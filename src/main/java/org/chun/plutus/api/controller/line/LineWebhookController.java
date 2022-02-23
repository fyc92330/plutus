package org.chun.plutus.api.controller.line;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chun.plutus.util.JsonBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import retrofit2.http.Header;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/webhook")
public class LineWebhookController {

  @PostMapping("/line/callback")
  public void lineCallBack(@RequestBody Map<String, Object> param, @RequestHeader(name = "x-line-signature") String signature) throws JsonProcessingException {
    System.out.println("Webhook In");
    System.out.println(signature);
    System.out.println(JsonBean.Extra.objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(param));
  }
}
