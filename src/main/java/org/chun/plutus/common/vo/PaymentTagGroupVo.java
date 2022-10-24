package org.chun.plutus.common.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.chun.plutus.common.vo.base.PaymentTagGroupBaseVo;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PaymentTagGroupVo extends PaymentTagGroupBaseVo {

  public PaymentTagGroupVo(Long tagGroupNum, String tagGroupName, String tagGroupColor) {
    super.setTagGroupNum(tagGroupNum);
    super.setTagGroupName(tagGroupName);
    super.setTagGroupColor(tagGroupColor);
  }

  private List<PaymentTagVo> paymentTagVoList;
}
