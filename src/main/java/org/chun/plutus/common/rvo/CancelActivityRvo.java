package org.chun.plutus.common.rvo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.chun.plutus.common.vo.ActivitySetVo;

import java.util.List;

@Getter
@Setter
@ToString
public class CancelActivityRvo {

  private Long actNum;

  private String actTitle;

  private Long hostUserNum;

  private List<ActivitySetVo> activitySetVoList;
}
