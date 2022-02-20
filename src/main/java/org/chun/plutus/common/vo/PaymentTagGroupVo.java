package org.chun.plutus.common.vo;

import lombok.Getter;
import lombok.Setter;
import org.chun.plutus.common.vo.base.PaymentTagGroupBaseVo;

import java.util.List;

@Getter
@Setter
public class PaymentTagGroupVo extends PaymentTagGroupBaseVo {

  private List<PaymentTagVo> paymentTagVoList;
}
