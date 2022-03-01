package org.chun.plutus.api.service;

import com.linecorp.bot.model.event.CallbackRequest;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.UnfollowEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chun.plutus.api.mod.LineMessageMod;
import org.chun.plutus.api.mod.UserMod;
import org.chun.plutus.common.vo.AppUserVo;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class LineMessageService {

  private final UserMod userMod;
  private final LineMessageMod lineMessageMod;

  public void handleLineCallbackRequest(CallbackRequest request) {

    for (Event lineEvent : request.getEvents()) {

      final AppUserVo appUserVo = userMod.saveAppUser(lineEvent.getSource().getUserId());
      final Long userNum = appUserVo.getUserNum();
      final String userId = appUserVo.getUserLineId();

      if (lineEvent instanceof MessageEvent) {
        System.out.println("MESSAGE");

      } else if (lineEvent instanceof FollowEvent) {
        System.out.println("FOLLOW event.");
        final String replyToken = ((FollowEvent) lineEvent).getReplyToken();
        lineMessageMod.sendFirstWelcomeMessage(replyToken, userId);
        userMod.handleFollowEvent(userNum);
      } else if (lineEvent instanceof UnfollowEvent) {
        userMod.handleUnFollowEvent(userNum);
      }

    }
  }


}
