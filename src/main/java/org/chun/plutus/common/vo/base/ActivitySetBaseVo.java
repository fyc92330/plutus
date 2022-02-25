package org.chun.plutus.common.vo.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivitySetBaseVo {

  /** 活動群組流水號 */
  private Long acsNum;
  /** 對應活動流水號 */
  private Long actNum;
  /** 活動參與使用者流水號 */
  private Long userNum;
  /** 活動群組狀態(1:參與中,9:已離開) */
  private String status;
  /** 活動群組開始時間 */
  private String startDate;
  /** 活動群組結束時間 */
  private String endDate;

}
