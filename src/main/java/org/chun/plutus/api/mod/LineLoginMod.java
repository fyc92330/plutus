package org.chun.plutus.api.mod;

import org.chun.plutus.util.MapUtil;
import org.chun.plutus.util.MomentUtil;
import org.chun.plutus.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

/**
 * 測試這裡是否作為message api的進入點
 */
@Slf4j
@Service
public class LineLoginMod {

  private static final okhttp3.MediaType jsonMediaType = okhttp3.MediaType.parse(MediaType.APPLICATION_JSON_UTF8_VALUE);

  @Value("${line.login.login-page-uri}")
  private String redirectUrl;

  @Value("${line.login.client-id}")
  private String loginClientId;

  @Value("${line.login.scope}")
  private String scope;

  @Value("${line.login.redirect-uri}")
  private String codeBackUri;

  /**
   * 導轉登入器頁面
   *
   * @return
   */
  public String getRedirectParams() {
    Map<String, String> params = MapUtil.newHashMap(
        "response_type", "code",
        "client_id", loginClientId,
        "redirect_uri", codeBackUri,
        "state", MomentUtil.Date.yyyy_MM_dd.format(LocalDate.now()),
        "scope", scope
    );

    StringBuilder sb = new StringBuilder(redirectUrl.concat("?"));
    params.entrySet().stream()
        .map(entry -> StringUtil.concat(entry.getKey(), "=", entry.getValue(), "&"))
        .forEach(sb::append);

    return sb.substring(0, sb.length() - 1);
  }
}
