package org.chun.plutus.common.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.chun.plutus.common.vo.base.PaymentRecordMTagBaseVo;

@Getter
@Setter
@NoArgsConstructor
public class PaymentRecordMTagVo extends PaymentRecordMTagBaseVo {

  public PaymentRecordMTagVo(Long paymentNum, Long tagGroupNum, Long tagNum) {
    super.setPaymentNum(paymentNum);
    super.setTagGroupNum(tagGroupNum);
    super.setTagNum(tagNum);
  }
}
