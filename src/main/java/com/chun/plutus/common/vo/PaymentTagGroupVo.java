package com.chun.plutus.common.vo;

import com.chun.plutus.common.vo.base.PaymentTagGroupBaseVo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PaymentTagGroupVo extends PaymentTagGroupBaseVo {

  private List<PaymentTagVo> paymentTagVoList;
}
