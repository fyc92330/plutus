package org.chun.plutus.api.service;

import com.linecorp.bot.model.event.CallbackRequest;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.UnfollowEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chun.plutus.api.helper.LineMessageHelper;
import org.chun.plutus.api.mod.ActivityMod;
import org.chun.plutus.api.mod.UserMod;
import org.chun.plutus.common.constant.LineChannelViewConst;
import org.chun.plutus.common.enums.JoinCodePrefixEnum;
import org.chun.plutus.common.vo.AppUserVo;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@Service
public class LineMessageService {

  private final UserMod userMod;
  private final ActivityMod activityMod;
  private final LineMessageHelper lineMessageHelper;

  public void handleLineCallbackRequest(CallbackRequest request) {

    for (Event lineEvent : request.getEvents()) {

      final AppUserVo appUserVo = userMod.saveAppUser(lineEvent.getSource().getUserId());
      final Long userNum = appUserVo.getUserNum();
      final String userId = appUserVo.getUserLineId();

      if (lineEvent instanceof MessageEvent) {
        handleMessageEvent((MessageEvent) lineEvent, appUserVo);
      } else if (lineEvent instanceof FollowEvent) {
        final String replyToken = ((FollowEvent) lineEvent).getReplyToken();
        lineMessageHelper.sendFirstWelcomeMessage(replyToken, userId);
        userMod.handleFollowEvent(userNum);
      } else if (lineEvent instanceof UnfollowEvent) {
        userMod.handleUnFollowEvent(userNum);
      }

    }
  }


  private void handleMessageEvent(MessageEvent event, AppUserVo appUserVo) {
    MessageContent message = event.getMessage();
    if (message instanceof TextMessageContent) {
      handleTextMessage(event, appUserVo.getUserNum());
    }
  }

  private void handleTextMessage(MessageEvent<TextMessageContent> event, Long userNum) {
    final String text = event.getMessage().getText();
    Arrays.stream(JoinCodePrefixEnum.values())
        .map(JoinCodePrefixEnum::val)
        .filter(text::startsWith)
        .forEach(prefix -> activityMod.handleJoinCodeText(event, userNum, prefix));

    if(text.equals(LineChannelViewConst.SHOW_QRCODE_SYMBOL)) activityMod.replyQrcode(event, userNum);
  }

}
