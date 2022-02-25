package org.chun.plutus.common.vo.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityBaseVo {

  /** 活動流水號 */
  private Long actNum;
  /** 使用者流水號 */
  private Long userNum;
  /** 活動標題 */
  private String actTitle;
  /** 活動描述 */
  private String actDesc;
  /** 活動開始時間 */
  private String startDate;
  /** 活動結束時間 */
  private String endDate;
  /** 活動參加碼 */
  private String joinCode;
  /** 活動建立人 */
  private String createUser;
  /** 活動建立日期 */
  private String createDate;

}
