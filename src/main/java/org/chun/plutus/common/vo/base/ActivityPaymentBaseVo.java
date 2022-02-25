package org.chun.plutus.common.vo.base;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ActivityPaymentBaseVo {

  /** 活動收款流水號 */
  private Long acpNum;
  /** 對應活動流水號 */
  private Long actNum;
  /** 對應活動明細流水號 */
  private Long acdNum;
  /** 付款使用者流水號 */
  private Long userNum;
  /** 付款金額 */
  private BigDecimal price;
  /** 是否付款 */
  private String isPaid;
  /** 付款日期 */
  private String payDate;

}
