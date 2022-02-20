package org.chun.plutus.common.qo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.chun.plutus.common.vo.base.BaseVo;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppUserLoginQo extends BaseVo {

  private String accessToken;
}
