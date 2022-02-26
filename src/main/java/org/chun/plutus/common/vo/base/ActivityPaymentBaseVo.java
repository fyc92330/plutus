package org.chun.plutus.common.vo.base;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Do not modify this file!
 * For extending functions, edit the ActivityPaymentVo file please.
 */
public class ActivityPaymentBaseVo extends BaseVo implements Serializable, Cloneable {

  public ActivityPaymentBaseVo() {
  }

  /** 活動收支編號 */
  private Long acpNum;

  /** 活動主檔編號 */
  private Long actNum;

  /** 活動明細編號 */
  private Long acdNum;

  /** 使用者編號 */
  private Long userNum;

  /** 收支金額 */
  private BigDecimal price;

  /** 是否付款 */
  private String isPaid;

  /** 付款日期 */
  private String payDate;

  public Long getAcpNum() {
    return this.acpNum;
  }

  public void setAcpNum(Long acpNum) {
    this.acpNum = acpNum;
  }

  public Long getActNum() {
    return this.actNum;
  }

  public void setActNum(Long actNum) {
    this.actNum = actNum;
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

  public BigDecimal getPrice() {
    return this.price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public String getIsPaid() {
    return this.isPaid;
  }

  public void setIsPaid(String isPaid) {
    this.isPaid = isPaid;
  }

  public String getPayDate() {
    return this.payDate;
  }

  public void setPayDate(String payDate) {
    this.payDate = payDate;
  }


}
