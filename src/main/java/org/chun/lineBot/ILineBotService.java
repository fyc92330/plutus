package org.chun.lineBot;

import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.profile.UserProfileResponse;

public interface ILineBotService {

  UserProfileResponse profile(String userId);

  void reply(ReplyMessage replyMessage, String userId);

  void push(PushMessage pushMessage);

  void menuChange(String userId, String menuId);
}
