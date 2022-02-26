package org.chun.plutus.common.vo;

import lombok.Getter;
import lombok.Setter;
import org.chun.plutus.common.vo.base.ActivitySetBaseVo;

@Getter
@Setter
public class ActivitySetVo extends ActivitySetBaseVo {

  private String actTitle;

  private String hostUserName;
}
