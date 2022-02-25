package org.chun.line.client;

import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.error.ErrorResponse;
import com.linecorp.bot.model.profile.UserProfileResponse;

public interface ILineBotService {

  UserProfileResponse profile(String userId);

  ErrorResponse reply(ReplyMessage replyMessage, String userId);

  ErrorResponse push(PushMessage pushMessage);
}
