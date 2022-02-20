package org.chun.plutus.common.vo_remove_after;

import org.chun.plutus.common.vo_remove_after.base.UserInfoBaseVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoVo extends UserInfoBaseVo {

  /** 使用者財務狀況 */
  private UserFinancialVo userFinancialVo;
}
