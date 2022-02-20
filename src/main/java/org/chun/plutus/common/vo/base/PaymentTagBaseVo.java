package org.chun.plutus.common.vo.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentTagBaseVo {

  /** 標籤流水號 */
  private Long tagNum;
  /** 標籤所屬標籤群流水號 */
  private Long tagGroupNum;
  /** 標籤名稱 */
  private String tagName;
  /** 標籤顏色 */
  private String tagColor;

}
