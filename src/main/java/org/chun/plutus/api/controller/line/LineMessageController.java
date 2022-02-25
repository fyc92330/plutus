package org.chun.plutus.api.controller.line;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.linecorp.bot.model.event.CallbackRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chun.plutus.api.service.LineMessageService;
import org.chun.plutus.util.JsonBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/webhook")
public class LineMessageController {

  private final LineMessageService lineMessageService;

  @PostMapping("/line/callback")
  public void lineCallBack(@RequestBody CallbackRequest request, @RequestHeader(name = "x-line-signature") String signature) throws JsonProcessingException {
    log.info("signature:{},\nrequest:\n{}", signature, JsonBean.Extra.objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(request));
    lineMessageService.handleLineCallbackRequest(request);
  }
}
