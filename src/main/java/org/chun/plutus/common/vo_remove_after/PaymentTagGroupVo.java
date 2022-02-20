package org.chun.plutus.common.vo_remove_after;

import org.chun.plutus.common.vo_remove_after.base.PaymentTagGroupBaseVo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PaymentTagGroupVo extends PaymentTagGroupBaseVo {

  private List<PaymentTagVo> paymentTagVoList;
}
