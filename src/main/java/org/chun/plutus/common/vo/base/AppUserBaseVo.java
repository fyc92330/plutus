package org.chun.plutus.common.vo.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppUserBaseVo extends BaseVo {

  /** 使用者流水號 */
  private Long userNum;
  /** 使用者姓名 */
  private String userName;
  /** 使用者描述(LINE的名稱) */
  private String userDesc;
  /** 使用者性別(1:男,2:女,3:不顯示) */
  private String userGender;
  /** 使用者手機 */
  private String userMobile;
  /** 使用者信箱 */
  private String userEmail;
  /** 使用者LINE ID */
  private String lineUserId;
  /** 使用者密碼 */
  private String userPwd;

}
