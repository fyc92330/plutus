package org.chun.plutus.api.controller.line;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.chun.line.client.ILineLoginService;
import org.chun.line.model.LineApiClientIdResponse;
import org.chun.line.model.LineApiProfileResponse;
import org.chun.line.model.LineApiTokenResponse;
import org.chun.plutus.api.mod.LineLoginMod;
import org.chun.plutus.util.JsonBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/line")
public class LineLoginController {

  private final LineLoginMod lineLoginMod;

  @Qualifier("lineLoginService")
  private final ILineLoginService lineLoginService;

  @GetMapping("/gate")
  public void test(@RequestParam String code, @RequestParam String state) {
    log.info("==============================GET IN!!!!!!!!");
    log.info("code:{}, state:{}", code, state);
  }

  /**
   * 導轉登入器頁面
   */
  @SneakyThrows
  @GetMapping("/loginPage")
  public void redirectLoginPage(HttpServletResponse response) {
    final String redirectUrl = lineLoginMod.getRedirectParams();
    log.info("redirect to url : {}", redirectUrl);
    response.sendRedirect(redirectUrl);
  }

  @PostMapping("/user")
  public void getUser(@RequestParam Map<String, String> params) {
    LineApiTokenResponse tokenResponse = JsonBean.Extra.objectMapper().convertValue(params, LineApiTokenResponse.class);
    final String accessToken = tokenResponse.getAccessToken();
    final String tokenType = tokenResponse.getTokenType();
    LineApiClientIdResponse clientIdResponseBody =
        lineLoginService.verify(accessToken, tokenType);
    log.info("CLIENT_ID_OBJECT:{}", clientIdResponseBody);

    LineApiProfileResponse profileResponseBody =
        lineLoginService.profile(accessToken, tokenType);
    log.info("PROFILE_OBJECT:{}", profileResponseBody);
  }

}
