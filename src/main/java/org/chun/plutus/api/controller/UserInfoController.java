package org.chun.plutus.api.controller;

import org.chun.plutus.common.vo1.UserFinancialVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/user")
public class UserInfoController {


  /**
   * 取得使用者資訊
   * 回傳isInit, N可進行初次設定
   *
   * @param request
   * @return
   */
  @GetMapping("/profile")
  public Object getUserProfile(HttpServletRequest request) {
    final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
    return null;
  }

  @PostMapping("/profile")
  public void initUserProfile(@RequestBody UserFinancialVo userFinancialVo) {

  }
}
