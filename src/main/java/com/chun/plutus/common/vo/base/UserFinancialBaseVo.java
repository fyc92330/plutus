package com.chun.plutus.common.vo.base;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UserFinancialBaseVo {

  /** 使用者財務流水號 */
  private Long financeNum;
  /** 使用者流水號 */
  private Long userNum;
  /** 發薪日期 */
  private String incomeDate;
  /** 收入金額 */
  private BigDecimal income;
}
