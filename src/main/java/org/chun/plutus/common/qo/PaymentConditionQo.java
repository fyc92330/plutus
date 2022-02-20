package org.chun.plutus.common.qo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentConditionQo {

  private Long userNum;

  private String startDate;

  private String endDate;

  private String recordType;

}
