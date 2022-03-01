package org.chun.plutus.common.vo.base;

import java.io.Serializable;

/**
 * Do not modify this file!
 * For extending functions, edit the AppUserMemberStatusVo file please.
 */
public class AppUserMemberStatusBaseVo extends BaseVo implements Serializable, Cloneable {

  public AppUserMemberStatusBaseVo() {
  }

  /** 狀態紀錄編號 */
  private Long aumsNum;

  /** 使用者編號 */
  private Long userNum;

  /** 使用者狀態(1:使用中,2:已移除) */
  private String userStatus;

  /** 建立日期 */
  private String createDate;

  /** 追蹤日期 */
  private String followDate;

  /** 取消追蹤日期 */
  private String unFollowDate;

  /** 取消追蹤次數 */
  private Long unFollowTime;

  public Long getAumsNum() {
    return this.aumsNum;
  }

  public void setAumsNum(Long aumsNum) {
    this.aumsNum = aumsNum;
  }

  public Long getUserNum() {
    return this.userNum;
  }

  public void setUserNum(Long userNum) {
    this.userNum = userNum;
  }

  public String getUserStatus() {
    return this.userStatus;
  }

  public void setUserStatus(String userStatus) {
    this.userStatus = userStatus;
  }

  public String getCreateDate() {
    return this.createDate;
  }

  public void setCreateDate(String createDate) {
    this.createDate = createDate;
  }

  public String getFollowDate() {
    return this.followDate;
  }

  public void setFollowDate(String followDate) {
    this.followDate = followDate;
  }

  public String getUnFollowDate() {
    return this.unFollowDate;
  }

  public void setUnFollowDate(String unFollowDate) {
    this.unFollowDate = unFollowDate;
  }

  public Long getUnFollowTime() {
    return this.unFollowTime;
  }

  public void setUnFollowTime(Long unFollowTime) {
    this.unFollowTime = unFollowTime;
  }


}
