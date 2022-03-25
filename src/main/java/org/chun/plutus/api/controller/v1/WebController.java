package org.chun.plutus.api.controller.v1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequestMapping
public class WebController {

  @GetMapping("/")
  public String web() {
    log.info("web!!!");
    return "index";
  }
}
