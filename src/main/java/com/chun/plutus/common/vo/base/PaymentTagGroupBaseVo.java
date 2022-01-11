package com.chun.plutus.common.vo.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentTagGroupBaseVo {

  /** 標籤群流水號 */
  private Long tagGroupNum;
  /** 標籤群名稱 */
  private String tagGroupName;
  /** 建立日期 */
  private String createDate;
  /** 異動日期 */
  private String updateDate;
}
