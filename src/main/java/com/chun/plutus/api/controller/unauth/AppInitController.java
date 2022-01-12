package com.chun.plutus.api.controller.unauth;

import com.chun.plutus.constant.CacheConst;
import com.chun.plutus.util.CacheUtil;
import io.undertow.util.Headers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/unauth")
public class AppInitController {

  private final CacheUtil cacheUtil;

  @GetMapping("/user")
  public void userConfirm(HttpServletRequest request) {
    final String jwtToken = request.getHeader(Headers.AUTHORIZATION_STRING);
    cacheUtil.getObjectFromCache(CacheConst.USER_INFO_CACHE,jwtToken);
  }
}
