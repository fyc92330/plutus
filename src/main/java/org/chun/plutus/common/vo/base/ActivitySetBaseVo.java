package org.chun.plutus.common.vo.base;

import java.io.Serializable;

/**
 * Do not modify this file!
 * For extending functions, edit the ActivitySetVo file please.
 */
public class ActivitySetBaseVo extends BaseVo implements Serializable, Cloneable {

  public ActivitySetBaseVo() {
  }

  /** 活動分群編號 */
  private Long acsNum;

  /** 活動主檔編號 */
  private Long actNum;

  /** 使用者編號 */
  private Long userNum;

  /** 使用者狀態(0:邀請中,1:參加中,2:拒絕邀請,8:已取消,9:已離開) */
  private String status;

  /** 開始時間 */
  private String startDate;

  /** 結束時間 */
  private String endDate;

  public Long getAcsNum() {
    return this.acsNum;
  }

  public void setAcsNum(Long acsNum) {
    this.acsNum = acsNum;
  }

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

  public String getStatus() {
    return this.status;
  }

  public void setStatus(String status) {
    this.status = status;
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


}
