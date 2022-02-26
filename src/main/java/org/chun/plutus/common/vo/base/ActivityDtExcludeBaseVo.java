package org.chun.plutus.common.vo.base;

import java.io.Serializable;

/**
 * Do not modify this file!
 * For extending functions, edit the ActivityDtExcludeVo file please.
 */
public class ActivityDtExcludeBaseVo extends BaseVo implements Serializable, Cloneable {

  public ActivityDtExcludeBaseVo() {
  }

  /** 活動分帳排除編號 */
  private Long aceNum;

  /** 活動明細編號 */
  private Long acdNum;

  /** 排除使用者編號 */
  private Long userNum;

  /** 排除原因 */
  private String reason;

  public Long getAceNum() {
    return this.aceNum;
  }

  public void setAceNum(Long aceNum) {
    this.aceNum = aceNum;
  }

  public Long getAcdNum() {
    return this.acdNum;
  }

  public void setAcdNum(Long acdNum) {
    this.acdNum = acdNum;
  }

  public Long getUserNum() {
    return this.userNum;
  }

  public void setUserNum(Long userNum) {
    this.userNum = userNum;
  }

  public String getReason() {
    return this.reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }


}
