package com.chun.plutus.api.controller.line;

import com.chun.line.client.ILineLoginService;
import com.chun.plutus.api.mod.LineLoginMod;
import com.chun.plutus.common.dto.LineAccessTokenDto;
import com.chun.plutus.common.dto.LineClientVerifyDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/line")
public class LineLoginController {

  private final LineLoginMod lineLoginMod;

  @Qualifier("lineLoginService")
  private ILineLoginService lineLoginService;

  @SneakyThrows
  @PostMapping("/callback")
  public void lineCallBack() {
    log.info("webhook in");
  }

  @SneakyThrows
  @GetMapping("/call")
  public void testCall(HttpServletRequest request) {
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

  /**
   * 登入取得access token
   *
   * @param code
   * @param state
   */
  @SneakyThrows
  @GetMapping("/login")
  public void lineOauthLogin(@RequestParam String code, @RequestParam String state) {
    log.info("code:{}, state:{}", code, state);
    LineAccessTokenDto lineAccessTokenDto = lineLoginMod.getAccessToken(code);
//
//    final String accessToken = StringUtil.concat(lineAccessTokenDto.getTokenType(), " ", lineAccessTokenDto.getAccessToken());
//    LineClientVerifyDto lineClientVerifyDto = lineLoginMod.getLineClientId(accessToken);
//    LineUserProfileDto lineUserProfileDto = lineLoginMod.getLineUserProfile(accessToken);

    LineClientVerifyDto lineClientVerifyDto = lineLoginService.authorizationToken(lineAccessTokenDto, "Content-Type");
  }

}
