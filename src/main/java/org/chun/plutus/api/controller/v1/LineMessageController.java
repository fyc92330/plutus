package org.chun.plutus.api.controller.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.linecorp.bot.model.event.CallbackRequest;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.JoinEvent;
import com.linecorp.bot.model.event.LeaveEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.ReplyEvent;
import com.linecorp.bot.model.event.UnfollowEvent;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.event.source.UserSource;
import com.linecorp.bot.model.message.TextMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.chun.plutus.api.facade.MenuActionFacade;
import org.chun.plutus.api.facade.MessageFacade;
import org.chun.plutus.api.helper.LineMessageHelper;
import org.chun.plutus.api.mod.UserMod;
import org.chun.plutus.common.dto.LineUserDto;
import org.chun.plutus.common.vo.AppUserVo;
import org.chun.plutus.util.JsonBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/webhook")
public class LineMessageController {

  private final MessageFacade messageFacade;
  private final MenuActionFacade menuActionFacade;
  private final UserMod userMod;
  private final LineMessageHelper lineMessageHelper;

  /**
   * LINE訊息入口
   *
   * @param request
   * @param signature
   * @throws JsonProcessingException
   */
  @PostMapping("/line/callback")
  public String lineCallBack(@RequestBody CallbackRequest request, @RequestHeader(name = "x-line-signature") String signature) throws JsonProcessingException {
    log.info("signature:{},\nrequest:\n{}", signature, JsonBean.Extra.objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(request));
    handleLineCallbackRequest(request);
    return "index";
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
      LineUserDto lineUserDto = null;
      Long userNum = null;
      final String userId = Optional.ofNullable(lineEvent.getSource())
          .filter(source -> source instanceof UserSource)
          .map(Source::getUserId)
          .orElse(Strings.EMPTY);

      if (StringUtils.isNotBlank(userId)) {
        lineUserDto = this.genUserProfile(userId, lineEvent);
        userNum = lineUserDto.getUserNum();
      }

      if (lineEvent instanceof PostbackEvent) {
        menuActionFacade.handlePostbackEvent((PostbackEvent) lineEvent, lineUserDto);
      } else if (lineEvent instanceof MessageEvent) {
        messageFacade.handleMessageEvent((MessageEvent) lineEvent, lineUserDto);
      } else if (lineEvent instanceof FollowEvent) {
        final String replyToken = ((FollowEvent) lineEvent).getReplyToken();
        lineMessageHelper.sendFirstWelcomeMessage(replyToken, userId);
        userMod.handleFollowEvent(userNum);
      } else if (lineEvent instanceof UnfollowEvent) {
        userMod.handleUnFollowEvent(userNum);
      } else if (lineEvent instanceof JoinEvent) {
        log.warn("JOIN EVENT!!");
        lineMessageHelper.replyMessage(new TextMessage("我來了"), ((JoinEvent) lineEvent).getReplyToken(), null);
      } else if (lineEvent instanceof LeaveEvent) {
        log.warn("LEAVE EVENT!!");
      }

    }
  }

  /**
   * 取得使用者資訊
   *
   * @param userId
   * @return
   */
  private LineUserDto genUserProfile(String userId, Event event) {
    final AppUserVo appUserVo = userMod.saveAppUser(userId);
    LineUserDto.LineUserDtoBuilder userDtoBuilder = LineUserDto.builder();
    if (event instanceof ReplyEvent) {
      userDtoBuilder.replyToken(((ReplyEvent) event).getReplyToken());
    }
    return userDtoBuilder
        .userNum(appUserVo.getUserNum())
        .userId(userId)
        .build();
  }

}
