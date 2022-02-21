package org.chun.plutus.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chun.plutus.api.mod.PaymentMod;
import org.chun.plutus.common.mo.PaymentRecordMo;
import org.chun.plutus.common.qo.PaymentConditionQo;
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
@RequestMapping("/api/payment")
public class PaymentController {

  private final PaymentMod paymentMod;

  /**
   * 查詢紀錄
   *
   * @param paymentConditionQo
   * @return
   */
  @RequestMapping(value = "/list", method = {GET, POST})
  public Object queryPayments(@RequestBody PaymentConditionQo paymentConditionQo) {
    return paymentMod.query(paymentConditionQo);
  }

  /**
   * 新增紀錄
   *
   * @param paymentRecordMo
   */
  @RequestMapping(value = "/record", method = {GET, POST})
  public void createPayment(@RequestBody PaymentRecordMo paymentRecordMo) {
    paymentMod.createPaymentRecord(paymentRecordMo);
  }

  /**
   * 編輯紀錄
   *
   * @param paymentRecordMo
   */
  @RequestMapping(value = "/record", method = {PUT})
  public void editPayment(@RequestBody PaymentRecordMo paymentRecordMo) {
    paymentMod.editPaymentRecord(paymentRecordMo);
  }

  /**
   * 刪除紀錄
   *
   * @param paymentNum
   */
  @RequestMapping(value = "/record/{paymentNum}", method = {DELETE})
  public void removePayment(@PathVariable Long paymentNum) {
    paymentMod.removePaymentRecord(paymentNum);
  }

}
