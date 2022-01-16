package com.chun.plutus.api.controller.line;

import com.chun.plutus.util.BeanUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lineBot")
public class LineBotController {



  @GetMapping("/auth")
  public void getAccessToken() throws IOException {
    final String clientId = "1656805064";
    final String redirectUri = "https%3A%2F%2F82b3-2001-b011-400b-7246-950f-a030-11af-ce22.ngrok.io%2Fcallback";

    HttpUrl httpUrl = new HttpUrl.Builder()
        .scheme("https")
        .host("access.line.me")
        .addPathSegment("oauth2")
        .addPathSegment("v2.1")
        .addPathSegment("authorize")
        .addQueryParameter("response_type", "code")
        .addQueryParameter("client_id", clientId)
        .addQueryParameter("redirect_uri", redirectUri)
        .addQueryParameter("state", "gor156don")
        .addQueryParameter("scope", "profile")
        .build();

    OkHttpClient client = new OkHttpClient().newBuilder().build();
    Request req = new Request.Builder()
        .url(httpUrl)
        .get().build();

    Response res = client.newCall(req).execute();
    Map<String, Object> body = BeanUtil.objectMapper()
        .readValue(res.body().string(), new TypeReference<Map<String, Object>>() {
        });
    System.out.println(BeanUtil.objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(body));
  }
}
