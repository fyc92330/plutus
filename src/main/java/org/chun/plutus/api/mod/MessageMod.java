package org.chun.plutus.api.mod;

import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chun.lineBot.ILineBotService;
import org.chun.plutus.common.constant.LineCommonMessageConst;
import org.chun.plutus.common.dao.AppUserDao;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.LocalDateTime;

import static org.chun.plutus.util.MomentUtil.DateTime.yyyy_MM_dd_HH_mm;

@Slf4j
@RequiredArgsConstructor
@Service
public class MessageMod {

  private final ILineBotService lineBotService;
  private final AppUserDao appUserDao;


  /**
   * 活動建立成功訊息
   *
   * @param replyToken
   * @param userId
   * @param joinCode
   */
  public void createSuccessMessage(String replyToken, String userId, String joinCode) {
    final TextMessage textMessage = new TextMessage(String.format(LineCommonMessageConst.CREATE_SUCCESS, joinCode));
    replyMessage(textMessage, replyToken, userId);
  }

  public void joinSuccessMessage() {

  }


  /** =================================================== version2 ================================================== */


//  /**
//   * 發送詢問是否加入活動的模板訊息
//   *
//   * @param inviteJoinCodeMo
//   */
//  public void pushInviteTemplateMessage(InviteJoinCodeMo inviteJoinCodeMo) {
//    final String joinCode = inviteJoinCodeMo.getJoinCode();
//    ConfirmTemplate template = new ConfirmTemplate(
//        String.format("%s已邀請您加入活動(%s)", inviteJoinCodeMo.getUserName(), inviteJoinCodeMo.getActTitle()),
//        new MessageAction(LineChannelViewConst.INVITE_RESPONSE_STR, JoinCodeUtil.genInviteJoinCode(joinCode)),
//        new MessageAction(LineChannelViewConst.CANCEL_RESPONSE_STR, JoinCodeUtil.genCancelJoinCode(joinCode))
//    );
//    TemplateMessage templateMessage = new TemplateMessage("", template);
//    inviteJoinCodeMo.getUserNumList().stream()
//        .map(appUserDao::getByPk)
//        .map(AppUserVo::getUserLineId)
//        .filter(Objects::nonNull)
//        .forEach(userId -> this.pushMessage(templateMessage, userId));
//  }


  /**
   * 回傳加入活動訊息
   *
   * @param actTitle
   * @param replyToken
   * @param userId
   */
  public void sendJoinNotify(String actTitle, String replyToken, String userId) {
    final String msg = String.format("您已加入活動:%s,\n加入時間:%s", actTitle, yyyy_MM_dd_HH_mm.format(LocalDateTime.now()));
    replyMessage(new TextMessage(msg), replyToken, userId);
  }

  /**
   * 回傳離開活動訊息
   *
   * @param actTitle
   * @param replyToken
   * @param userId
   */
  public void sendLeaveNotify(String actTitle, String replyToken, String userId) {
    final String msg = String.format("您已離開活動:%s,\n離開時間:%s", actTitle, yyyy_MM_dd_HH_mm.format(LocalDateTime.now()));
    replyMessage(new TextMessage(msg), replyToken, userId);
  }

  /**
   * 回傳取消活動訊息
   *
   * @param actTitle
   * @param userId
   */
  public void sendCancelNotify(String actTitle, String userId) {
    final String msg = String.format("活動:%s已被取消,\n取消時間:%s", actTitle, yyyy_MM_dd_HH_mm.format(LocalDateTime.now()));
    pushMessage(new TextMessage(msg), userId);
  }

  /**
   * 回傳拒絕活動訊息
   *
   * @param actTitle
   * @param userId
   */
  public void sendRejectNotify(String actTitle, String userId) {
    final String msg = String.format("您已拒絕活動:%s,\n拒絕時間:%s", actTitle, yyyy_MM_dd_HH_mm.format(LocalDateTime.now()));
    pushMessage(new TextMessage(msg), userId);
  }

  /**
   * 回傳qrcode
   *
   * @param imagePath
   * @param replyToken
   * @param userId
   */
  public void replyQrcode(URI imagePath, String replyToken, String userId) {
    replyMessage(new ImageMessage(imagePath, imagePath), replyToken, userId);
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
