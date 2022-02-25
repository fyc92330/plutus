package org.chun.plutus.api.mod;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.chun.line.client.ILineLoginService;
import org.chun.line.model.LineApiClientIdResponse;
import org.chun.line.model.LineApiProfileResponse;
import org.chun.plutus.common.dao.AppUserDao;
import org.chun.plutus.common.exceptions.LineClientUserNotFoundException;
import org.chun.plutus.common.vo.AppUserVo;
import org.chun.plutus.util.JsonBean;
import org.chun.plutus.util.TokenUtil;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserMod {

  private static final String clientId = "1656805593";

  private static final String TOKEN_TYPE = "Bearer";

  private final AppUserDao appUserDao;
  private final ILineLoginService lineLoginService;

  @SneakyThrows
  public String confirmAppUser(final String accessToken) {
    final String targetClientId = Optional.ofNullable(lineLoginService.verify(accessToken, TOKEN_TYPE))
        .map(LineApiClientIdResponse::getClientId)
        .filter(this::isCurrentClient)
        .orElseThrow(LineClientUserNotFoundException::new);

    final AppUserVo appUserVo = Optional.ofNullable(lineLoginService.profile(accessToken, TOKEN_TYPE))
        .map(LineApiProfileResponse::getUserId)
        .map(appUserDao::getByUserId)
        .orElseThrow(() -> new LineClientUserNotFoundException(targetClientId));

    final String jsonStr = JsonBean.Extra.objectMapper().writeValueAsString(appUserVo);
    return TokenUtil.genJwtToken(jsonStr, appUserVo.getUserNum());
  }

  /** =================================================== private ================================================== */


  private boolean isCurrentClient(final String targetClientId) {
    return clientId.equals(targetClientId);
  }
}
