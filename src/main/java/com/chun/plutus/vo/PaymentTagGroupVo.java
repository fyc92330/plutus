package com.chun.plutus.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class PaymentTagGroupVo {

  /** 標籤群流水號 */
  private Long tagGroupNum;
  /** 標籤群名稱 */
  private String tagGroupName;
  /** 建立日期 */
  private String createDate;
  /** 異動日期 */
  private String updateDate;


  /** =================================================== query item ================================================== */

  private List<PaymentTagVo> paymentTagVoList;
}
