package org.chun.plutus.common.rvo;

import lombok.Getter;
import lombok.Setter;
import org.chun.plutus.common.vo.ActivityDtVo;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ActivityViewRvo {

  /** 活動編號 */
  private Long actNum;
  /** 活動標題 */
  private String actTitle;
  /** 活動描述 */
  private String actDesc;
  /** 主辦人姓名 */
  private String hostUserName;
  /** 活動開始時間 */
  private String startDate;
  /** 目前參與人數 */
  private Long userCount;
  /** 目前消費金額 */
  private BigDecimal currentCost;
  /** 活動明細(acdNum,acdTitle,startDate,endDate,cost) */
  private List<ActivityDtVo> activityDtVoList;

}
