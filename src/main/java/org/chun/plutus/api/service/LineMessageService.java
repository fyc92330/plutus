package org.chun.plutus.api.service;

import com.linecorp.bot.model.event.CallbackRequest;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.profile.UserProfileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chun.lineBot.ILineBotService;
import org.chun.plutus.common.dao.AppUserDao;
import org.chun.plutus.common.vo.AppUserVo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class LineMessageService {

  private final ILineBotService lineBotService;

  private final AppUserDao appUserDao;

  public void handleLineCallbackRequest(CallbackRequest request) {

    for (Event lineEvent : request.getEvents()) {

      final AppUserVo appUserVo = this.getAppUser(lineEvent.getSource().getUserId());

      if (lineEvent instanceof MessageEvent) {
        System.out.println("MESSAGE");

      } else if (lineEvent instanceof FollowEvent) {
        System.out.println("FOLLOW event.");

      }

    }
  }

  /** =================================================== private ================================================== */


  private AppUserVo getAppUser(String userId) {
    return Optional.ofNullable(appUserDao.getByUserId(userId))
        .orElseGet(() -> {
          UserProfileResponse userProfileResponse = lineBotService.profile(userId);
          AppUserVo appUserVo = new AppUserVo();
          appUserVo.setUserName(userProfileResponse.getDisplayName());
          appUserVo.setUserLineId(userId);
          appUserVo.setUserLinePic(userProfileResponse.getPictureUrl().toString());
          appUserDao.insert(appUserVo);
          return appUserVo;
        });
  }

}
