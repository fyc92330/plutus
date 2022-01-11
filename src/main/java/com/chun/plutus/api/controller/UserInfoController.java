package com.chun.plutus.api.controller;

import com.chun.plutus.common.vo.UserInfoVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/user")
public class UserInfoController {

  /**
   * header取出token, 撈出相對應的user
   *
   * @param request
   * @return
   */
  @GetMapping("/user")
  public UserInfoVo getUserInfo(HttpServletRequest request){

    return null;
  }

}
