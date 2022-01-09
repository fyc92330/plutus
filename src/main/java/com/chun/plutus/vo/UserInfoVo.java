package com.chun.plutus.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserInfoVo {

  /** 使用者編號 */
  private Long userNum;
  /** 使用者名稱 */
  private String userName;
  /** 使用者生日 */
  private String userBirthday;
  /** 使用者電話 */
  private String userMobile;
  /** 使用者憑證 */
  private String userToken;

  /** =================================================== query item ================================================== */

  /** 使用者財務狀況 */
  private UserFinancialVo userFinancialVo;
}
