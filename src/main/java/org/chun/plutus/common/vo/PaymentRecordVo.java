package org.chun.plutus.common.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.chun.plutus.common.mo.PaymentRecordMo;
import org.chun.plutus.common.vo.base.PaymentRecordBaseVo;

@Getter
@Setter
@NoArgsConstructor
public class PaymentRecordVo extends PaymentRecordBaseVo {

  public PaymentRecordVo(PaymentRecordMo paymentRecordMo) {
    super.setUserNum(paymentRecordMo.getUserNum());
    super.setPaymentNum(paymentRecordMo.getPaymentNum());
    super.setPaymentTitle(paymentRecordMo.getPaymentTitle());
    super.setPaymentDesc(paymentRecordMo.getPaymentDesc());
    super.setPaymentCost(paymentRecordMo.getPaymentCost());
    super.setRecordType(paymentRecordMo.getRecordType());
  }
}
