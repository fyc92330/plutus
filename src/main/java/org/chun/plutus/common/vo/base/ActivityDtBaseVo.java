package org.chun.plutus.common.vo.base;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ActivityDtBaseVo {

  /** 活動明細流水號 */
  private Long acdNum;
  /** 對應活動流水號 */
  private Long actNum;
  /** 活動明細標題 */
  private String acdTitle;
  /** 活動明細開始時間 */
  private String startDate;
  /** 活動明細結束時間 */
  private String endDate;
  /** 活動明細花費 */
  private BigDecimal cost;
  /** 活動明細拆帳類型(1:均分,2:依時間分,3:各自付款) */
  private String payType;
  /** 預先付費使用者 */
  private Long prePaidUser;

}
