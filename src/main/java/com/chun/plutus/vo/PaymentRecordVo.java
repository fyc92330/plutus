package com.chun.plutus.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PaymentRecordVo {

  /** 帳本紀錄流水號 */
  private Long paymentNum;
  /** 使用者編號 */
  private Long userNum;
  /** 紀錄標題 */
  private String paymentTitle;
  /** 紀錄內容 */
  private String paymentContent;
  /** 建立日期 */
  private String createDate;
  /** 異動日期 */
  private String updateDate;
}
