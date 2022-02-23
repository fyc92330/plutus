package org.chun.plutus.api.controller.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chun.plutus.api.mod.TagGroupMod;
import org.chun.plutus.common.dao.PaymentTagDao;
import org.chun.plutus.common.mo.TagGroupMo;
import org.chun.plutus.common.vo.PaymentTagVo;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/symbol")
public class PaymentTagGroupController {

  private final TagGroupMod tagGroupMod;
  private final PaymentTagDao paymentTagDao;

  /**
   * 初始化設定頁面
   *
   * @param userNum
   * @return
   */
  @GetMapping("/init/{userNum}")
  public Object init(@PathVariable Long userNum) {
    return tagGroupMod.listOwnerGroupListWithTags(userNum);
  }

  /**
   * 建立標籤群
   *
   * @param tagGroupMo
   */
  @PostMapping("/group")
  public void createPaymentTagGroup(@RequestBody TagGroupMo tagGroupMo) {
    tagGroupMod.createTagGroup(tagGroupMo);
  }

  /**
   * 編輯標籤群
   *
   * @param tagGroupMo
   */
  @PutMapping("/group")
  public void editPaymentTagGroup(@RequestBody TagGroupMo tagGroupMo) {
    tagGroupMod.editTagGroup(tagGroupMo);
  }

  /**
   * 移除標籤群
   *
   * @param tagGroupNum
   */
  @DeleteMapping("/group/{tagGroupNum}")
  public void removePaymentTagGroup(@PathVariable Long tagGroupNum) {
    tagGroupMod.removeTagGroup(tagGroupNum);
  }

  /**
   * 新增標籤
   *
   * @param paymentTagVo
   */
  @PostMapping("/tag")
  public void createSingleTag(@RequestBody PaymentTagVo paymentTagVo) {
    paymentTagDao.insert(paymentTagVo);
  }

  /**
   * 移除標籤
   *
   * @param tagNum
   */
  @DeleteMapping("/tag/{tagNum}")
  public void removeSingleTag(@PathVariable Long tagNum) {
    tagGroupMod.removePaymentTag(tagNum);
  }

}
