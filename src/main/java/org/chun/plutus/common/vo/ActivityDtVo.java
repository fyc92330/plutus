package org.chun.plutus.common.vo;

import lombok.Getter;
import lombok.Setter;
import org.chun.plutus.common.vo.base.ActivityDtBaseVo;

@Getter
@Setter
public class ActivityDtVo extends ActivityDtBaseVo {

  private String payerName;

  private String actStatus;
}
