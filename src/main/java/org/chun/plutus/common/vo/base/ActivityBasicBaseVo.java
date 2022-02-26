package org.chun.plutus.common.vo.base;

import java.io.Serializable;

/**
 * Do not modify this file!
 * For extending functions, edit the ActivityBasicVo file please.
 */
public class ActivityBasicBaseVo extends BaseVo implements Serializable, Cloneable {

  public ActivityBasicBaseVo() {
  }

  /** 活動編號 */
  private Long actNum;

  /** 活動發起人編號 */
  private Long userNum;

  /** 活動標題 */
  private String actTitle;

  /** 活動描述 */
  private String actDesc;

  /** 活動開始時間 */
  private String startDate;

  /** 活動結束時間 */
  private String endDate;

  /** 活動邀請碼 */
  private String joinCode;

  /** 活動狀態(0:準備中,1:進行中,9:結束) */
  private String actStatus;

  /** 活動建立日期 */
  private String createDate;

  public Long getActNum() {
    return this.actNum;
  }

  public void setActNum(Long actNum) {
    this.actNum = actNum;
  }

  public Long getUserNum() {
    return this.userNum;
  }

  public void setUserNum(Long userNum) {
    this.userNum = userNum;
  }

  public String getActTitle() {
    return this.actTitle;
  }

  public void setActTitle(String actTitle) {
    this.actTitle = actTitle;
  }

  public String getActDesc() {
    return this.actDesc;
  }

  public void setActDesc(String actDesc) {
    this.actDesc = actDesc;
  }

  public String getStartDate() {
    return this.startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getEndDate() {
    return this.endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public String getJoinCode() {
    return this.joinCode;
  }

  public void setJoinCode(String joinCode) {
    this.joinCode = joinCode;
  }

  public String getActStatus() {
    return this.actStatus;
  }

  public void setActStatus(String actStatus) {
    this.actStatus = actStatus;
  }

  public String getCreateDate() {
    return this.createDate;
  }

  public void setCreateDate(String createDate) {
    this.createDate = createDate;
  }


}
