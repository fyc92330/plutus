package org.chun.plutus.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chun.plutus.api.mod.TagGroupMod;
import org.chun.plutus.common.dao.PaymentTagDao;
import org.chun.plutus.common.mo.TagGroupMo;
import org.chun.plutus.common.vo.PaymentTagVo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

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
  @RequestMapping(value = "/init/{userNum}", method = {GET})
  public Object init(@PathVariable Long userNum) {
    return tagGroupMod.listOwnerGroupListWithTags(userNum);
  }

  /**
   * 建立標籤群
   *
   * @param tagGroupMo
   */
  @RequestMapping(value = "/group", method = {POST})
  public void createPaymentTagGroup(@RequestBody TagGroupMo tagGroupMo) {
    tagGroupMod.createTagGroup(tagGroupMo);
  }

  /**
   * 編輯標籤群
   *
   * @param tagGroupMo
   */
  @RequestMapping(value = "/group", method = {PUT})
  public void editPaymentTagGroup(@RequestBody TagGroupMo tagGroupMo) {
    tagGroupMod.editTagGroup(tagGroupMo);
  }

  /**
   * 移除標籤群
   *
   * @param tagGroupNum
   */
  @RequestMapping(value = "/group/{tagGroupNum}", method = {DELETE})
  public void removePaymentTagGroup(@PathVariable Long tagGroupNum) {
    tagGroupMod.removeTagGroup(tagGroupNum);
  }

  /**
   * 新增標籤
   *
   * @param paymentTagVo
   */
  @RequestMapping(value = "/tag", method = {POST})
  public void createSingleTag(@RequestBody PaymentTagVo paymentTagVo) {
    paymentTagDao.insert(paymentTagVo);
  }

  /**
   * 移除標籤
   *
   * @param tagNum
   */
  @RequestMapping(value = "/tag/{tagNum}", method = {DELETE})
  public void removeSingleTag(@PathVariable Long tagNum) {
    tagGroupMod.removePaymentTag(tagNum);
  }

}
