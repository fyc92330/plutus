package com.chun.plutus.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PaymentRecordMTagVo {

  /** 紀錄對應標籤流水號 */
  private Long prmtNum;
  /** 帳本紀錄流水號 */
  private Long paymentNum;
  /** 標籤群流水號 */
  private Long tagGroupNum;
  /** 標籤流水號 */
  private Long tagNum;
}
