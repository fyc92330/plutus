package org.chun.plutus.common.vo.base;

import java.io.Serializable;

/**
 * Do not modify this file!
 * For extending functions, edit the PaymentTagGroupVo file please.
 */
public class PaymentTagGroupBaseVo extends BaseVo implements Serializable, Cloneable {

  public PaymentTagGroupBaseVo() {
  }

  /** 標籤群編號 */
  private Long tagGroupNum;

  /** 標籤群名稱 */
  private String tagGroupName;

  /** 標籤群顏色 */
  private String tagGroupColor;

  /** 標籤群所有人編號 */
  private Long userNum;

  public Long getTagGroupNum() {
    return this.tagGroupNum;
  }

  public void setTagGroupNum(Long tagGroupNum) {
    this.tagGroupNum = tagGroupNum;
  }

  public String getTagGroupName() {
    return this.tagGroupName;
  }

  public void setTagGroupName(String tagGroupName) {
    this.tagGroupName = tagGroupName;
  }

  public String getTagGroupColor() {
    return this.tagGroupColor;
  }

  public void setTagGroupColor(String tagGroupColor) {
    this.tagGroupColor = tagGroupColor;
  }

  public Long getUserNum() {
    return this.userNum;
  }

  public void setUserNum(Long userNum) {
    this.userNum = userNum;
  }


}
