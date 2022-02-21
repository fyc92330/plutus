package org.chun.plutus.api.mod;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chun.plutus.common.dao.PaymentRecordDao;
import org.chun.plutus.common.dao.PaymentRecordMTagDao;
import org.chun.plutus.common.dao.PaymentTagDao;
import org.chun.plutus.common.dao.PaymentTagGroupDao;
import org.chun.plutus.common.mo.PaymentRecordMo;
import org.chun.plutus.common.qo.PaymentConditionQo;
import org.chun.plutus.common.vo.PaymentRecordMTagVo;
import org.chun.plutus.common.vo.PaymentRecordVo;
import org.chun.plutus.common.vo.PaymentTagGroupVo;
import org.chun.plutus.common.vo.PaymentTagVo;
import org.chun.plutus.util.DaoValidationUtil;
import org.chun.plutus.util.MapUtil;
import org.chun.plutus.util.MomentUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentMod {

  private final PaymentRecordDao paymentRecordDao;
  private final PaymentTagGroupDao paymentTagGroupDao;
  private final PaymentTagDao paymentTagDao;
  private final PaymentRecordMTagDao paymentRecordMTagDao;

  /**
   * 查詢紀錄
   *
   * @param paymentConditionQo
   * @return
   */
  public List<PaymentRecordVo> query(PaymentConditionQo paymentConditionQo) {
    return paymentRecordDao.listByQueryCondition(paymentConditionQo);
  }

  /**
   * 建立帳務紀錄
   *
   * @param paymentRecordMo
   */
  public void createPaymentRecord(PaymentRecordMo paymentRecordMo) {
    // 建立紀錄主檔
    PaymentRecordVo paymentRecordVo = new PaymentRecordVo(paymentRecordMo);
    paymentRecordDao.insert(paymentRecordVo);
    // 加上標籤關聯
    Optional.ofNullable(paymentRecordMo.getPaymentTagGroupVoList())
        .ifPresent(list -> this.saveRecordTags(list, paymentRecordVo.getPaymentNum()));
  }

  /**
   * 編輯帳務紀錄
   *
   * @param paymentRecordMo
   */
  public void editPaymentRecord(PaymentRecordMo paymentRecordMo) {
    // 編輯紀錄主檔
    PaymentRecordVo paymentRecordVo = new PaymentRecordVo(paymentRecordMo);
    paymentRecordVo.setUpdateDate(MomentUtil.DateTime.yyyy_MM_dd_HH_mm_ss.format(LocalDateTime.now()));
    paymentRecordDao.update(paymentRecordVo);
    final Long paymentNum = paymentRecordMo.getPaymentNum();
    // 先清除所有的標籤
    this.removeRecordTagRelation(paymentNum);
    // 加上標籤關聯
    Optional.ofNullable(paymentRecordMo.getPaymentTagGroupVoList())
        .ifPresent(list -> this.saveRecordTags(list, paymentNum));
  }

  /**
   * 移除帳務紀錄
   *
   * @param paymentNum
   */
  public void removePaymentRecord(Long paymentNum) {
    // 先清除所有的標籤
    this.removeRecordTagRelation(paymentNum);
    // 移除紀錄
    DaoValidationUtil.validateResultIsOne(() -> paymentRecordDao.deleteByPk(paymentNum));
  }


  /** =================================================== private ================================================== */

  /**
   * 儲存記錄對應的標籤
   *
   * @param paymentTagGroupVoList
   * @param paymentNum
   */
  private void saveRecordTags(List<PaymentTagGroupVo> paymentTagGroupVoList, Long paymentNum) {
    for (PaymentTagGroupVo tagGroupVo : paymentTagGroupVoList) {
      final Long tagGroupNum = tagGroupVo.getTagGroupNum();
      // 建立新的紀錄標籤組
      if (tagGroupNum == null) {
        paymentTagGroupDao.insert(tagGroupVo);
      }
      for (PaymentTagVo tagVo : tagGroupVo.getPaymentTagVoList()) {
        final Long tagNum = tagVo.getTagNum();
        // 建立新的標籤
        if (tagVo.getTagNum() == null) {
          tagVo.setTagGroupNum(tagGroupNum);
          paymentTagDao.insert(tagVo);
        }

        // 紀錄標籤關聯
        PaymentRecordMTagVo paymentRecordMTagVo = new PaymentRecordMTagVo(paymentNum, tagGroupNum, tagNum);
        paymentRecordMTagDao.insert(paymentRecordMTagVo);
      }
    }
  }

  /**
   * 移除紀錄時移除所有標籤關聯
   *
   * @param paymentNum
   */
  private void removeRecordTagRelation(Long paymentNum) {
    paymentRecordMTagDao.query(MapUtil.newHashMap("paymentNum", paymentNum)).stream()
        .findAny()
        .map(PaymentRecordMTagVo::getPrmtNum)
        .ifPresent(prmtNum -> DaoValidationUtil.validateResultIsOne(() -> paymentRecordMTagDao.deleteByPk(prmtNum), prmtNum));
  }
}
