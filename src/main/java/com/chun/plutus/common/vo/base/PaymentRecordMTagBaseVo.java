package com.chun.plutus.common.vo.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRecordMTagBaseVo {

  /** 紀錄對應標籤流水號 */
  private Long prmtNum;
  /** 帳本紀錄流水號 */
  private Long paymentNum;
  /** 標籤群流水號 */
  private Long tagGroupNum;
  /** 標籤流水號 */
  private Long tagNum;
}
