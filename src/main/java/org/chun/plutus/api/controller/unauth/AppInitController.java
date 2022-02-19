package org.chun.plutus.api.controller.unauth;

import org.chun.plutus.util.BeanUtil;
import io.undertow.util.Headers;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/unauth")
public class AppInitController {

//  private final CacheUtil cacheUtil;

  @GetMapping("/user")
  public void userConfirm(HttpServletRequest request) {
    final String jwtToken = request.getHeader(Headers.AUTHORIZATION_STRING);
//    cacheUtil.getObjectFromCache(CacheConst.USER_INFO_CACHE,jwtToken);
  }

  @SneakyThrows
  @GetMapping("/test")
  public void testSession(HttpSession session, HttpServletResponse response) {
    final String attrName = "test";
    final Object obj = session.getAttribute(attrName);
    if (obj == null) {
      log.info("miss session");
    } else {
      log.info(BeanUtil.Extra.objectMapper().writeValueAsString(obj));
    }

    test();
//    final String url = "https://8e0e-2001-b011-381a-72bf-a8e1-a0ff-6fdb-7516.ngrok.io/sso_callback";
//    response.sendRedirect(url);
  }

  @SneakyThrows
  private void test() {
    HttpUrl httpUrl = new HttpUrl.Builder().scheme("https").host("8e0e-2001-b011-381a-72bf-a8e1-a0ff-6fdb-7516.ngrok.io")
        .addPathSegment("sso_callback").build();

    FormBody formBody = new FormBody.Builder().add("tt", "cc").build();

    OkHttpClient client = new OkHttpClient().newBuilder().build();
    Request req = new Request.Builder().url(httpUrl).post(formBody).build();

    Response res = client.newCall(req).execute();
    log.info(BeanUtil.Extra.objectMapper().writeValueAsString(res.body()));
  }


}
