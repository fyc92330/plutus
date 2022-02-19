package org.chun.plutus.common.vo1;

import org.chun.plutus.common.vo1.base.PaymentTagGroupBaseVo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PaymentTagGroupVo extends PaymentTagGroupBaseVo {

  private List<PaymentTagVo> paymentTagVoList;
}
