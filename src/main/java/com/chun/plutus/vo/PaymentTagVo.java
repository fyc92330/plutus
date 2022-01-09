package com.chun.plutus.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PaymentTagVo {

  /** 標籤流水號 */
  private Long tagNum;
  /** 標籤所屬標籤群流水號 */
  private Long tagGroupNum;
  /** 標籤名稱 */
  private String tagName;
  /** 標籤顏色 */
  private String tagColor;
  /** 建立日期 */
  private String createDate;
  /** 異動日期 */
  private String updateDate;
}
