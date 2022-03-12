package org.chun.plutus.common.rvo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.chun.plutus.common.vo.ActivityDtVo;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ToString
public class ActivityViewRvo {

  /** 活動編號 */
  private Long actNum;
  /** 活動主標題 */
  private String actTitle;
  /** 主辦人姓名 */
  private String hostUserName;
  /** 活動開始時間 */
  private String startDate;
  /** 目前參與人數 */
  private Long userCount;
  /** 目前消費金額 */
  private BigDecimal currentCost;
  /** 使用者姓名 */
  private String userName;
  /** 現在時間 */
  private String nowDate;

  /** 子活動資訊 */
  private List<ActivityDtVo> activityDtVoList;
  /** 是否為主辦人 */
  private Boolean isHost;

// v2 啟用屬性
//  /** 活動描述 */
//  private String actDesc;
//  /** 活動明細(acdNum,acdTitle,startDate,endDate,cost) */
//  private List<ActivityDtVo> activityDtVoList;

}
