package org.chun.plutus.common.vo.base;

import java.io.Serializable;

/**
 * Do not modify this file!
 * For extending functions, edit the AppUserVo file please.
 */
public class AppUserBaseVo extends BaseVo implements Serializable, Cloneable {

  public AppUserBaseVo() {
  }

  /** 使用者編號 */
  private Long userNum;

  /** 使用者姓名 */
  private String userName;

  /** 使用者性別(1:男,2:女,3:不顯示) */
  private String userGender;

  /** 使用者電話 */
  private String userMobile;

  /** 使用者信箱 */
  private String userEmail;

  /** 使用者密碼 */
  private String userPwd;

  /** 使用者LINE名稱 */
  private String userLineName;

  /** 使用者LINE userId */
  private String userLineId;

  /** 使用者LINE照片路徑 */
  private String userLinePic;

  public Long getUserNum() {
    return this.userNum;
  }

  public void setUserNum(Long userNum) {
    this.userNum = userNum;
  }

  public String getUserName() {
    return this.userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getUserGender() {
    return this.userGender;
  }

  public void setUserGender(String userGender) {
    this.userGender = userGender;
  }

  public String getUserMobile() {
    return this.userMobile;
  }

  public void setUserMobile(String userMobile) {
    this.userMobile = userMobile;
  }

  public String getUserEmail() {
    return this.userEmail;
  }

  public void setUserEmail(String userEmail) {
    this.userEmail = userEmail;
  }

  public String getUserPwd() {
    return this.userPwd;
  }

  public void setUserPwd(String userPwd) {
    this.userPwd = userPwd;
  }

  public String getUserLineName() {
    return this.userLineName;
  }

  public void setUserLineName(String userLineName) {
    this.userLineName = userLineName;
  }

  public String getUserLineId() {
    return this.userLineId;
  }

  public void setUserLineId(String userLineId) {
    this.userLineId = userLineId;
  }

  public String getUserLinePic() {
    return this.userLinePic;
  }

  public void setUserLinePic(String userLinePic) {
    this.userLinePic = userLinePic;
  }


}
