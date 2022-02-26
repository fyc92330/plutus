package org.chun.plutus.common.vo.base;

import java.io.Serializable;

/**
 * Do not modify this file!
 * For extending functions, edit the PaymentRecordMTagVo file please.
 */
public class PaymentRecordMTagBaseVo extends BaseVo implements Serializable, Cloneable {

  public PaymentRecordMTagBaseVo() {
  }

  /** 財務紀錄標籤關聯編號 */
  private Long prmtNum;

  /** 財務紀錄編號 */
  private Long paymentNum;

  /** 標籤群編號 */
  private Long tagGroupNum;

  /** 標籤編號 */
  private Long tagNum;

  public Long getPrmtNum() {
    return this.prmtNum;
  }

  public void setPrmtNum(Long prmtNum) {
    this.prmtNum = prmtNum;
  }

  public Long getPaymentNum() {
    return this.paymentNum;
  }

  public void setPaymentNum(Long paymentNum) {
    this.paymentNum = paymentNum;
  }

  public Long getTagGroupNum() {
    return this.tagGroupNum;
  }

  public void setTagGroupNum(Long tagGroupNum) {
    this.tagGroupNum = tagGroupNum;
  }

  public Long getTagNum() {
    return this.tagNum;
  }

  public void setTagNum(Long tagNum) {
    this.tagNum = tagNum;
  }


}
