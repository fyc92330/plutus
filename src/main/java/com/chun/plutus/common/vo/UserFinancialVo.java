package com.chun.plutus.common.vo;

import com.chun.plutus.common.vo.base.UserFinancialBaseVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFinancialVo extends UserFinancialBaseVo {

  /** 發薪日詢問是否需要變更收入額度 */
  private boolean changeIncome;
}
