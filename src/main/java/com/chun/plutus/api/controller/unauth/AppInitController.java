package com.chun.plutus.api.controller.unauth;

import com.chun.plutus.vo.UserInfoVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/unauth")
public class AppInitController {

  @PostMapping("/user")
  public void userRegister(UserInfoVo userInfoVo){

  }
}
