package org.chun.plutus.common.vo1.base;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentRecordBaseVo {

  /** 帳本紀錄流水號 */
  private Long paymentNum;
  /** 使用者編號 */
  private Long userNum;
  /** 紀錄標題 */
  private String paymentTitle;
  /** 紀錄內容 */
  private String paymentContent;
  /** 紀錄金額 */
  private BigDecimal paymentCost;
  /** 紀錄類型(1:收入,2:支出) */
  private String recordType;
  /** 建立日期 */
  private String createDate;
  /** 異動日期 */
  private String updateDate;
}
