package org.chun.plutus.api.controller.v1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RequestMapping
public class WebController {

  @GetMapping("/")
  public Object web(){
    log.info("web!!!");
    return new ModelAndView();
  }
}
