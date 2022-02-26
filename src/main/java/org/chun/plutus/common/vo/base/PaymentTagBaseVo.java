package org.chun.plutus.common.vo.base;

import java.io.Serializable;

/**
 * Do not modify this file!
 * For extending functions, edit the PaymentTagVo file please.
 */
public class PaymentTagBaseVo extends BaseVo implements Serializable, Cloneable {

  public PaymentTagBaseVo() {
  }

  /** 標籤編號 */
  private Long tagNum;

  /** 標籤所屬群編號 */
  private Long tagGroupNum;

  /** 標籤名稱 */
  private String tagName;

  /** 標籤顏色 */
  private String tagColor;

  public Long getTagNum() {
    return this.tagNum;
  }

  public void setTagNum(Long tagNum) {
    this.tagNum = tagNum;
  }

  public Long getTagGroupNum() {
    return this.tagGroupNum;
  }

  public void setTagGroupNum(Long tagGroupNum) {
    this.tagGroupNum = tagGroupNum;
  }

  public String getTagName() {
    return this.tagName;
  }

  public void setTagName(String tagName) {
    this.tagName = tagName;
  }

  public String getTagColor() {
    return this.tagColor;
  }

  public void setTagColor(String tagColor) {
    this.tagColor = tagColor;
  }


}
