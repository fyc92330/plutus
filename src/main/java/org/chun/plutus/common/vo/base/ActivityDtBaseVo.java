package org.chun.plutus.common.vo.base;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Do not modify this file!
 * For extending functions, edit the ActivityDtVo file please.
 */
public class ActivityDtBaseVo extends BaseVo implements Serializable, Cloneable {

  public ActivityDtBaseVo() {
  }

  /** 活動明細編號 */
  private Long acdNum;

  /** 活動主檔編號 */
  private Long actNum;

  /** 活動明細標題 */
  private String acdTitle;

  /** 活動明細開始時間 */
  private String startDate;

  /** 活動明細結束時間 */
  private String endDate;

  /** 活動明細花費 */
  private BigDecimal cost;

  /** 活動明細拆帳方式 */
  private String payType;

  /** 活動明細預先付款人 */
  private Long prePaidUser;

  public Long getAcdNum() {
    return this.acdNum;
  }

  public void setAcdNum(Long acdNum) {
    this.acdNum = acdNum;
  }

  public Long getActNum() {
    return this.actNum;
  }

  public void setActNum(Long actNum) {
    this.actNum = actNum;
  }

  public String getAcdTitle() {
    return this.acdTitle;
  }

  public void setAcdTitle(String acdTitle) {
    this.acdTitle = acdTitle;
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

  public BigDecimal getCost() {
    return this.cost;
  }

  public void setCost(BigDecimal cost) {
    this.cost = cost;
  }

  public String getPayType() {
    return this.payType;
  }

  public void setPayType(String payType) {
    this.payType = payType;
  }

  public Long getPrePaidUser() {
    return this.prePaidUser;
  }

  public void setPrePaidUser(Long prePaidUser) {
    this.prePaidUser = prePaidUser;
  }


}
