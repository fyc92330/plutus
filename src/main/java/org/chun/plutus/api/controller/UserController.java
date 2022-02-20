package org.chun.plutus.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chun.plutus.api.mod.UserMod;
import org.chun.plutus.common.qo.AppUserLoginQo;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

  private final UserMod userMod;

  @RequestMapping(value = "/gate", method = {GET, POST})
  public void setAuthorization(@RequestBody AppUserLoginQo appUserLoginQo, HttpServletResponse response) {
    final String jwtToken = userMod.confirmAppUser(appUserLoginQo.getAccessToken());

    response.setHeader(HttpHeaders.AUTHORIZATION, jwtToken);
  }
}
