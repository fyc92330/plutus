package org.chun.plutus.api.helper;

import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chun.lineBot.ILineBotService;
import org.chun.plutus.common.constant.LineChannelViewConst;
import org.chun.plutus.common.dto.JoinCodeDto;
import org.chun.plutus.common.vo.ActivityBasicVo;
import org.chun.plutus.util.JoinCodeUtil;
import org.springframework.stereotype.Service;

import java.net.URI;

import static org.chun.plutus.common.constant.LineChannelViewConst.APP_VERSION;
import static org.chun.plutus.common.constant.LineCommonMessageConst.CONFIRM_CREATE;
import static org.chun.plutus.common.constant.LineCommonMessageConst.CREATE_SUCCESS;
import static org.chun.plutus.common.constant.LineCommonMessageConst.JOIN_SUCCESS;
import static org.chun.plutus.common.constant.LineCommonMessageConst.WELCOME_MESSAGE;

@Slf4j
@RequiredArgsConstructor
@Service
public class LineMessageHelper {

  private final ILineBotService lineBotService;

  /**
   * 歡迎訊息
   *
   * @param replyToken
   * @param userId
   */
  public void sendFirstWelcomeMessage(String replyToken, String userId) {
    replyMessage(new TextMessage(String.format(WELCOME_MESSAGE, APP_VERSION)), replyToken, userId);
  }

  /**
   * 有未結束的活動, 回傳詢問
   *
   * @param joinCodeDto
   */
  public void sendConfirmCreateMessage(JoinCodeDto joinCodeDto) {
    final String joinCode = joinCodeDto.getJoinCode();
    final String replyToken = joinCodeDto.getReplyToken();
    final String userId = joinCodeDto.getUserId();

    ConfirmTemplate template = new ConfirmTemplate(CONFIRM_CREATE,
        new MessageAction(LineChannelViewConst.YES_STR, JoinCodeUtil.genForceCreateCode(joinCode)),
        new MessageAction(LineChannelViewConst.NO_STR, JoinCodeUtil.genCancelJoinCode(joinCode))
    );
    replyMessage(new TemplateMessage("", template), replyToken, userId);
  }

  /**
   * 回傳活動建立成功訊息
   *
   * @param joinCodeDto
   */
  public void sendCreateSuccessMessage(JoinCodeDto joinCodeDto) {
    TextMessage textMessage = new TextMessage(String.format(CREATE_SUCCESS, joinCodeDto.getJoinCode()));
    replyMessage(textMessage, joinCodeDto.getReplyToken(), joinCodeDto.getUserId());
  }

  /**
   * 回傳錯誤訊息
   *
   * @param joinCodeDto
   * @param errorMsg
   */
  public void sendErrorMessage(JoinCodeDto joinCodeDto, String errorMsg) {
    this.sendTextMessage(joinCodeDto, errorMsg);
  }

  /**
   * 回傳加入成功訊息
   *
   * @param joinCodeDto
   * @param activityBasicVo
   */
  public void sendJoinSuccessMessage(JoinCodeDto joinCodeDto, ActivityBasicVo activityBasicVo) {
    TextMessage textMessage =
        new TextMessage(String.format(JOIN_SUCCESS, activityBasicVo.getHostUserName(), activityBasicVo.getActTitle()));
    replyMessage(textMessage, joinCodeDto.getReplyToken(), joinCodeDto.getUserId());
  }

  /**
   * 傳送文字訊息
   *
   * @param joinCodeDto
   * @param textContent
   */
  public void sendTextMessage(JoinCodeDto joinCodeDto, String textContent) {
    replyMessage(new TextMessage(textContent), joinCodeDto.getReplyToken(), joinCodeDto.getUserId());

  }

  /**
   * 傳送圖片訊息
   *
   * @param joinCodeDto
   * @param imageUrl
   */
  public void sendImageMessage(JoinCodeDto joinCodeDto, String imageUrl) {
    URI uri = URI.create(imageUrl);
    ImageMessage imageMessage = new ImageMessage(uri, uri);
    replyMessage(imageMessage, joinCodeDto.getReplyToken(), joinCodeDto.getUserId());
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
