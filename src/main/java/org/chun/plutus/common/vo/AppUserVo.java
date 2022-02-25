package org.chun.plutus.common.vo;

import lombok.Getter;
import lombok.Setter;
import org.chun.plutus.common.vo.base.AppUserBaseVo;

@Getter
@Setter
public class AppUserVo extends AppUserBaseVo {

  private String replyToken;
}
