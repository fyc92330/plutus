package org.chun.plutus.api.mod;

import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chun.lineBot.ILineBotService;
import org.chun.plutus.common.constant.LineChannelViewConst;
import org.chun.plutus.common.dao.AppUserDao;
import org.chun.plutus.common.mo.InviteJoinCodeMo;
import org.chun.plutus.common.vo.AppUserVo;
import org.chun.plutus.util.JoinCodeUtil;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class LineMessageMod {

  private static final String WELCOME_MESSAGE = "歡迎使用本app";

  private final ILineBotService lineBotService;
  private final AppUserDao appUserDao;

  /**
   * 發送詢問是否加入活動的模板訊息
   *
   * @param inviteJoinCodeMo
   */
  public void pushInviteTemplateMessage(InviteJoinCodeMo inviteJoinCodeMo) {
    final String joinCode = inviteJoinCodeMo.getJoinCode();
    ConfirmTemplate template = new ConfirmTemplate(
        String.format("%s已邀請您加入活動(%s)", inviteJoinCodeMo.getUserName(), inviteJoinCodeMo.getActTitle()),
        new MessageAction(LineChannelViewConst.INVITE_RESPONSE_STR, JoinCodeUtil.genInviteJoinCode(joinCode)),
        new MessageAction(LineChannelViewConst.CANCEL_RESPONSE_STR, JoinCodeUtil.genCancelJoinCode(joinCode))
    );
    TemplateMessage templateMessage = new TemplateMessage("", template);
    inviteJoinCodeMo.getUserNumList().stream()
        .map(appUserDao::getByPk)
        .map(AppUserVo::getUserLineId)
        .filter(Objects::nonNull)
        .forEach(userId -> this.pushMessage(templateMessage, userId));
  }

  /**
   * 歡迎訊息
   *
   * @param replyToken
   * @param userId
   */
  public void sendFirstWelcomeMessage(String replyToken, String userId) {
    TextMessage textMessage = new TextMessage(WELCOME_MESSAGE);
    replyMessage(textMessage, replyToken, userId);
  }

  /** =================================================== private ================================================== */

  /**
   * 回覆訊息, 失敗則用push的方式發送
   *
   * @param message
   * @param replyToken
   * @param userId
   */
  private void replyMessage(Message message, String replyToken, String userId) {
    lineBotService.reply(new ReplyMessage(replyToken, message), userId);
  }

  /**
   * 發送訊息
   *
   * @param message
   * @param userId
   */
  private void pushMessage(Message message, String userId) {
    lineBotService.push(new PushMessage(userId, message));
  }


}
