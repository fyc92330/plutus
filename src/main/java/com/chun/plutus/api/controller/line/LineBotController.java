package com.chun.plutus.api.controller.line;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lineBot")
public class LineBotController {

  @PostMapping("/callback")
  public void lineCallBack() {
    log.info("webhook in");
  }
}
