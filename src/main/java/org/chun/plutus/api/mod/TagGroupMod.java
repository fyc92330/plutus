//package org.chun.plutus.api.mod;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.chun.plutus.common.dao.PaymentTagDao;
//import org.chun.plutus.common.dao.PaymentTagGroupDao;
//import org.chun.plutus.common.mo.TagGroupMo;
//import org.chun.plutus.common.vo.PaymentTagGroupVo;
//import org.chun.plutus.common.vo.PaymentTagVo;
//import org.chun.plutus.util.DaoValidationUtil;
//import org.chun.plutus.util.MapUtil;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Slf4j
//@RequiredArgsConstructor
//@Service
//public class TagGroupMod {
//
//  private final PaymentTagGroupDao paymentTagGroupDao;
//  private final PaymentTagDao paymentTagDao;
//
//  /**
//   * 取得使用者自定義的標籤群與底下標籤
//   *
//   * @param userNum
//   * @return
//   */
//  public List<PaymentTagGroupVo> listOwnerGroupListWithTags(Long userNum) {
//    return paymentTagGroupDao.listGroupListWithTagsByUser(userNum);
//  }
//
//  /**
//   * 建立標籤群
//   *
//   * @param tagGroupMo
//   */
//  public void createTagGroup(TagGroupMo tagGroupMo) {
//    PaymentTagGroupVo paymentTagGroupVo =
//        new PaymentTagGroupVo(tagGroupMo.getTagGroupNum(), tagGroupMo.getTagGroupName(), tagGroupMo.getTagGroupColor());
//    paymentTagGroupDao.insert(paymentTagGroupVo);
//    final Long tagGroupNum = paymentTagGroupVo.getTagGroupNum();
//
//    // 新增標籤
//    tagGroupMo.getPaymentTagVoList().forEach(tag -> savePaymentTag(tag, tagGroupNum));
//  }
//
//  /**
//   * 編輯標籤群
//   *
//   * @param tagGroupMo
//   */
//  public void editTagGroup(TagGroupMo tagGroupMo) {
//    final Long tagGroupNum = tagGroupMo.getTagGroupNum();
//    PaymentTagGroupVo paymentTagGroupVo =
//        new PaymentTagGroupVo(tagGroupNum, tagGroupMo.getTagGroupName(), tagGroupMo.getTagGroupColor());
//    DaoValidationUtil.validateResultIsOne(() -> paymentTagGroupDao.update(paymentTagGroupVo), paymentTagGroupVo);
//
//    // 移除標籤
//    tagGroupMo.getRemoveTagNumList().forEach(this::removePaymentTag);
//    // 編輯標籤
//    for (PaymentTagVo paymentTagVo : tagGroupMo.getPaymentTagVoList()) {
//      final Long tagNum = paymentTagVo.getTagNum();
//      if (tagNum == null) {
//        paymentTagDao.insert(paymentTagVo);
//      } else {
//        DaoValidationUtil.validateResultIsOne(() -> paymentTagDao.update(paymentTagVo), paymentTagVo);
//      }
//    }
//
//  }
//
//  /**
//   * 移除標籤群
//   *
//   * @param tagGroupNum
//   */
//  public void removeTagGroup(Long tagGroupNum) {
//    // 先移除群底下標籤
//    paymentTagDao.query(MapUtil.newHashMap("tagGroupNum", tagGroupNum)).stream()
//        .map(PaymentTagVo::getTagNum)
//        .forEach(this::removePaymentTag);
//
//    // 移除
//    DaoValidationUtil.validateResultIsOne(() -> paymentTagGroupDao.deleteByPk(tagGroupNum));
//  }
//
//  /**
//   * 移除一筆標籤
//   *
//   * @param tagNum
//   */
//  public void removePaymentTag(Long tagNum) {
//    DaoValidationUtil.validateResultIsOne(() -> paymentTagDao.deleteByPk(tagNum));
//  }
//
//  /** =================================================== private ================================================== */
//
//  /**
//   * 新增一筆標籤
//   *
//   * @param paymentTagVo
//   * @param tagGroupNum
//   */
//  private void savePaymentTag(PaymentTagVo paymentTagVo, Long tagGroupNum) {
//    paymentTagVo.setTagGroupNum(tagGroupNum);
//    paymentTagDao.insert(paymentTagVo);
//  }
//
//}
