package org.chun.plutus.api.controller.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.linecorp.bot.model.event.CallbackRequest;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.UnfollowEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chun.plutus.api.facade.MessageFacade;
import org.chun.plutus.api.helper.LineMessageHelper;
import org.chun.plutus.api.mod.UserMod;
import org.chun.plutus.common.vo.AppUserVo;
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

  private final UserMod userMod;
  private final MessageFacade messageFacade;
  private final LineMessageHelper lineMessageHelper;

  /**
   * LINE訊息入口
   *
   * @param request
   * @param signature
   * @throws JsonProcessingException
   */
  @PostMapping("/line/callback")
  public void lineCallBack(@RequestBody CallbackRequest request, @RequestHeader(name = "x-line-signature") String signature) throws JsonProcessingException {
    log.info("signature:{},\nrequest:\n{}", signature, JsonBean.Extra.objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(request));
    handleLineCallbackRequest(request);
  }


  /** =================================================== private ================================================== */

  /**
   * 處理送進來的webhook
   *
   * @param request
   */
  private void handleLineCallbackRequest(CallbackRequest request) {
    for (Event lineEvent : request.getEvents()) {

      // 建立/取得使用者資訊
      final AppUserVo appUserVo = userMod.saveAppUser(lineEvent.getSource().getUserId());
      final Long userNum = appUserVo.getUserNum();
      final String userId = appUserVo.getUserLineId();

      if (lineEvent instanceof MessageEvent) {
        messageFacade.handleMessageEvent((MessageEvent) lineEvent, appUserVo);
      } else if (lineEvent instanceof FollowEvent) {
        final String replyToken = ((FollowEvent) lineEvent).getReplyToken();
        lineMessageHelper.sendFirstWelcomeMessage(replyToken, userId);
        userMod.handleFollowEvent(userNum);
      } else if (lineEvent instanceof UnfollowEvent) {
        userMod.handleUnFollowEvent(userNum);
      }

    }
  }


}
