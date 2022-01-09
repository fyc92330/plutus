package com.chun.plutus.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class UserFinancialVo {

  /** 使用者財務流水號 */
  private Long financeNum;
  /** 使用者流水號 */
  private Long userNum;
  /** 發薪日期 */
  private String incomeDate;
  /** 收入金額 */
  private BigDecimal income;
  /** 發薪日詢問是否需要變更收入額度 */
  private boolean changeIncome;
}
