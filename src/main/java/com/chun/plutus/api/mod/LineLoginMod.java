package com.chun.plutus.api.mod;

import com.chun.plutus.common.dto.LineAccessTokenDto;
import com.chun.plutus.common.dto.LineClientVerifyDto;
import com.chun.plutus.common.dto.LineUserProfileDto;
import com.chun.plutus.util.BeanUtil;
import com.chun.plutus.util.MapUtil;
import com.chun.plutus.util.MomentUtil;
import com.chun.plutus.util.StringUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import io.undertow.util.Headers;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

/**
 * 測試這裡是否作為message api的進入點
 */
@Slf4j
@Service
public class LineLoginMod {

  private static final okhttp3.MediaType jsonMediaType = okhttp3.MediaType.parse(MediaType.APPLICATION_JSON_UTF8_VALUE);

  @Value("${line.login.url}")
  private String redirectUrl;

  @Value("${line.login.client-id}")
  private String loginClientId;

  @Value("${line.scope}")
  private String scope;

  @Value("${line.test.uri}")
  private String codeBackUri;

  private String accessTokenBackUri;

  private String messageClientId;

  private String clientSecret;

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

  /**
   * 取得access token
   *
   * @param code
   * @return
   * @throws IOException
   */
  public LineAccessTokenDto getAccessToken(String code) throws IOException {

    HttpUrl httpUrl = new HttpUrl.Builder()
        .scheme("https")
        .host("api.line.me")
        .addPathSegment("oauth2")
        .addPathSegment("v2.1")
        .addPathSegment("token")
        .build();

    OkHttpClient okHttpClient = new OkHttpClient();

    Map<String, String> params = MapUtil.newHashMap(
        "grant_type", "authorization_code",
        "code", code,
        "redirect_uri", accessTokenBackUri,
        "client_id", messageClientId,
        "client_secret", clientSecret
    );

    RequestBody requestBody = RequestBody.create(jsonMediaType, BeanUtil.objectMapper().writeValueAsString(params));

    Request request = new Request.Builder()
        .addHeader(Headers.CONTENT_TYPE_STRING, "application/x-www-form-urlencoded")
        .url(httpUrl)
        .post(requestBody)
        .build();

    LineAccessTokenDto lineAccessTokenDto;

    try (Response response = okHttpClient.newCall(request).execute()) {
      lineAccessTokenDto = BeanUtil.objectMapper().convertValue(response.body(), LineAccessTokenDto.class);
    }

    return lineAccessTokenDto;
  }

  public LineClientVerifyDto getLineClientId(String accessToken) throws IOException {
    /** response from line */
    HttpUrl httpUrl = new HttpUrl.Builder()
        .scheme("https")
        .host("api.line.me")
        .addPathSegment("oauth2")
        .addPathSegment("v2.1")
        .addPathSegment("verify")
        .addQueryParameter("access_token", accessToken)
        .build();

    OkHttpClient okHttpClient = new OkHttpClient().newBuilder().build();
    Request request = new Request.Builder()
        .url(httpUrl)
        .get().build();

    LineClientVerifyDto lineClientVerifyDto;
    try(Response response = okHttpClient.newCall(request).execute()){
      lineClientVerifyDto = BeanUtil.objectMapper().convertValue(response.body(), LineClientVerifyDto.class);
    }

    return lineClientVerifyDto;
  }

  public LineUserProfileDto getLineUserProfile(String accessToken) throws IOException {
    HttpUrl httpUrl = new HttpUrl.Builder()
        .scheme("https")
        .host("api.line.me")
        .addPathSegment("v2")
        .addPathSegment("profile")
        .build();

    OkHttpClient client = new OkHttpClient.Builder().build();
    Request request = new Request.Builder()
        .addHeader(HttpHeaders.AUTHORIZATION, accessToken)
        .url(httpUrl)
        .get().build();

    LineUserProfileDto lineUserProfileDto;
    try(Response response = client.newCall(request).execute()){
      lineUserProfileDto = BeanUtil.objectMapper().convertValue(response.body(), LineUserProfileDto.class);
    }
    return lineUserProfileDto;
  }
}
