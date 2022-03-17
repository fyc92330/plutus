package org.chun.plutus.common.qo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PaymentConditionQo {

  /** 使用者流水號 */
  private Long userNum;
  /** 查詢期間起 */
  private String startDate;
  /** 查詢期間迄 */
  private String endDate;
  /** 紀錄類型 */
  private String recordType;

}
