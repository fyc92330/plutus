package org.chun.plutus.common.vo_remove_after.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoBaseVo {

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
}
