package org.chun.plutus.common.vo.base;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Do not modify this file!
 * For extending functions, edit the PaymentRecordVo file please.
 */
public class PaymentRecordBaseVo extends BaseVo implements Serializable, Cloneable {

  public PaymentRecordBaseVo() {
  }

  /** 財務紀錄編號 */
  private Long paymentNum;

  /** 財務紀錄所有人編號 */
  private Long userNum;

  /** 財務紀錄標題 */
  private String paymentTitle;

  /** 財務紀錄描述 */
  private String paymentDesc;

  /** 財務紀錄金額 */
  private BigDecimal paymentCost;

  /** 財務紀錄類型(1:收入,2:支出) */
  private String recordType;

  /** 財務紀錄發生日期 */
  private String recordDate;

  /** 財務紀錄建立日期 */
  private String createDate;

  /** 財務紀錄修改日期 */
  private String updateDate;

  public Long getPaymentNum() {
    return this.paymentNum;
  }

  public void setPaymentNum(Long paymentNum) {
    this.paymentNum = paymentNum;
  }

  public Long getUserNum() {
    return this.userNum;
  }

  public void setUserNum(Long userNum) {
    this.userNum = userNum;
  }

  public String getPaymentTitle() {
    return this.paymentTitle;
  }

  public void setPaymentTitle(String paymentTitle) {
    this.paymentTitle = paymentTitle;
  }

  public String getPaymentDesc() {
    return this.paymentDesc;
  }

  public void setPaymentDesc(String paymentDesc) {
    this.paymentDesc = paymentDesc;
  }

  public BigDecimal getPaymentCost() {
    return this.paymentCost;
  }

  public void setPaymentCost(BigDecimal paymentCost) {
    this.paymentCost = paymentCost;
  }

  public String getRecordType() {
    return this.recordType;
  }

  public void setRecordType(String recordType) {
    this.recordType = recordType;
  }

  public String getRecordDate() {
    return this.recordDate;
  }

  public void setRecordDate(String recordDate) {
    this.recordDate = recordDate;
  }

  public String getCreateDate() {
    return this.createDate;
  }

  public void setCreateDate(String createDate) {
    this.createDate = createDate;
  }

  public String getUpdateDate() {
    return this.updateDate;
  }

  public void setUpdateDate(String updateDate) {
    this.updateDate = updateDate;
  }


}
