package org.chun.plutus.api.helper;

import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.linecorp.bot.model.message.template.ImageCarouselColumn;
import com.linecorp.bot.model.message.template.ImageCarouselTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chun.lineBot.ILineBotService;
import org.chun.plutus.common.dto.LineUserDto;
import org.chun.plutus.common.dto.SubMenuImageDto;
import org.chun.plutus.common.vo.ActivityBasicVo;
import org.chun.plutus.util.StringUtil;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.chun.plutus.common.constant.LineChannelViewConst.APP_VERSION;
import static org.chun.plutus.common.constant.LineChannelViewConst.NO_STR;
import static org.chun.plutus.common.constant.LineChannelViewConst.QRCODE_INVITE_URL;
import static org.chun.plutus.common.constant.LineChannelViewConst.YES_STR;
import static org.chun.plutus.common.constant.LineCommonMessageConst.CONFIRM_CREATE;
import static org.chun.plutus.common.constant.LineCommonMessageConst.CREATE_SUCCESS;
import static org.chun.plutus.common.constant.LineCommonMessageConst.JOIN_SUCCESS;
import static org.chun.plutus.common.constant.LineCommonMessageConst.WELCOME_MESSAGE;
import static org.chun.plutus.common.enums.MenuEnum.Action.CANCEL;
import static org.chun.plutus.common.enums.MenuEnum.Action.FORCE_CREATE;
import static org.chun.plutus.common.enums.MenuEnum.Action.MENU;
import static org.chun.plutus.common.enums.MenuEnum.Setting.COST;
import static org.chun.plutus.common.enums.MenuEnum.Setting.PAYER;
import static org.chun.plutus.common.enums.MenuEnum.Setting.TITLE;
import static org.chun.plutus.common.enums.MenuEnum.Setting.TYPE_AVERAGE;
import static org.chun.plutus.common.enums.MenuEnum.Setting.TYPE_CHOICE;
import static org.chun.plutus.common.enums.MenuEnum.Setting.TYPE_SCALE;

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
   * @param lineUserDto
   */
  public void sendConfirmCreateMessage(LineUserDto lineUserDto) {

    ConfirmTemplate template = new ConfirmTemplate(CONFIRM_CREATE, Arrays.asList(
        new PostbackAction(YES_STR, FORCE_CREATE.val()),
        new PostbackAction(NO_STR, CANCEL.val())
    ));

    sendTemplateMessage(lineUserDto, new TemplateMessage("開啟新活動", template));
  }

  /**
   * 回傳活動建立成功訊息
   *
   * @param lineUserDto
   */
  public void sendCreateSuccessMessage(LineUserDto lineUserDto) {
    TextMessage textMessage = new TextMessage(String.format(CREATE_SUCCESS, lineUserDto.getJoinCode()));
    replyMessage(textMessage, lineUserDto.getReplyToken(), lineUserDto.getUserId());
  }

  /**
   * 回傳錯誤訊息
   *
   * @param lineUserDto
   * @param errorMsg
   */
  public void sendErrorMessage(LineUserDto lineUserDto, String errorMsg) {
    this.sendTextMessage(lineUserDto, errorMsg);
  }

  /**
   * 回傳加入成功訊息
   *
   * @param lineUserDto
   * @param activityBasicVo
   */
  public void sendJoinSuccessMessage(LineUserDto lineUserDto, ActivityBasicVo activityBasicVo) {
    TextMessage textMessage =
        new TextMessage(String.format(JOIN_SUCCESS, activityBasicVo.getHostUserName(), activityBasicVo.getActTitle()));
    replyMessage(textMessage, lineUserDto.getReplyToken(), lineUserDto.getUserId());
  }

  /**
   * 製作活動選單模板
   *
   * @param lineUserDto
   * @param subMenuImageDto
   * @return
   */
  public void sendMenuTemplateMessage(LineUserDto lineUserDto, SubMenuImageDto subMenuImageDto) {
    // 先製作設定圖片選單
    ImageCarouselTemplate imageCarouselTemplate = generateSettingTemplateMessage(subMenuImageDto);
    TemplateMessage settingTemplate = TemplateMessage.builder()
        .altText("設定選單")
        .template(imageCarouselTemplate)
        .build();

    // 製作拆帳設定選單
    CarouselColumn carouselColumn = CarouselColumn.builder()
        .thumbnailImageUrl(URI.create(subMenuImageDto.getTypeImageUrl()))
        .imageBackgroundColor("#FFFFFF")
        .title("拆帳方式設定")
        .text("設定拆帳方式")
        .actions(this.genPayTypeActions())
        .build();

    // 組裝拆帳設定選單
    CarouselTemplate carouselTemplate = CarouselTemplate.builder()
        .imageAspectRatio("square")
        .imageSize("contain")
        .columns(Collections.singletonList(carouselColumn))
        .build();
    TemplateMessage payTypeTemplate = TemplateMessage.builder()
        .altText("拆帳方式")
        .template(carouselTemplate)
        .build();

    this.sendTemplateMessage(lineUserDto, settingTemplate, payTypeTemplate);
  }

  /** =================================================== Message ================================================== */

  /**
   * 傳送文字訊息
   *
   * @param lineUserDto
   * @param textContent
   */
  public void sendTextMessage(LineUserDto lineUserDto, String textContent) {
    replyMessage(new TextMessage(textContent), lineUserDto.getReplyToken(), lineUserDto.getUserId());

  }

  /**
   * 傳送圖片訊息
   *
   * @param lineUserDto
   * @param imageUrl
   */
  public void sendImageMessage(LineUserDto lineUserDto, String imageUrl) {
    URI uri = URI.create(imageUrl);
    ImageMessage imageMessage = new ImageMessage(uri, uri);
    replyMessage(imageMessage, lineUserDto.getReplyToken(), lineUserDto.getUserId());
  }

  /**
   * 傳送模板訊息
   *
   * @param lineUserDto
   * @param templateMessages
   */
  public void sendTemplateMessage(LineUserDto lineUserDto, TemplateMessage... templateMessages) {
    replyMessages(Arrays.asList(templateMessages), lineUserDto.getReplyToken(), lineUserDto.getUserId());
  }

  /** =================================================== private ================================================== */

  /**
   * 建立設定圖片輪播模板
   *
   * @param subMenuImageDto
   * @return
   */
  private ImageCarouselTemplate generateSettingTemplateMessage(SubMenuImageDto subMenuImageDto) {

    List<ImageCarouselColumn> imageCarouselColumnList = new ArrayList<>();
    final String menu = MENU.val();
    // 設定標題
    URIAction setTitleAction = new URIAction("設定節點標題", URI.create(StringUtil.concat(QRCODE_INVITE_URL, menu, TITLE.val())), null);
    ImageCarouselColumn titleSetting = new ImageCarouselColumn(URI.create(subMenuImageDto.getTitleImageUrl()), setTitleAction);
    imageCarouselColumnList.add(titleSetting);

    // 設定消費金額
    URIAction setCostAction = new URIAction("設定節點消費", URI.create(StringUtil.concat(QRCODE_INVITE_URL, menu, COST.val())), null);
    ImageCarouselColumn castSetting = new ImageCarouselColumn(URI.create(subMenuImageDto.getCostImageUrl()), setCostAction);
    imageCarouselColumnList.add(castSetting);

    // 設定先付款人
    URIAction setPayerAction = new URIAction("設定付款人", URI.create(StringUtil.concat(QRCODE_INVITE_URL, menu, PAYER.val())), null);
    ImageCarouselColumn payerSetting = new ImageCarouselColumn(URI.create(subMenuImageDto.getDaddyImageUrl()), setPayerAction);
    imageCarouselColumnList.add(payerSetting);

    return new ImageCarouselTemplate(imageCarouselColumnList);
  }

  /**
   * 製作分帳設定選單的面板
   *
   * @return
   */
  private List<Action> genPayTypeActions() {
    final String menu = MENU.val();
    return Arrays.asList(
        new URIAction("平均分攤", URI.create(StringUtil.concat(QRCODE_INVITE_URL, menu, TYPE_AVERAGE.val())), null),
        new URIAction("按時間均分", URI.create(StringUtil.concat(QRCODE_INVITE_URL, menu, TYPE_SCALE.val())), null),
        new URIAction("指定分攤", URI.create(StringUtil.concat(QRCODE_INVITE_URL, menu, TYPE_CHOICE.val())), null)
    );
  }

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

  private void replyMessages(List<Message> messages, String replyToken, String userId) {
    lineBotService.reply(new ReplyMessage(replyToken, messages), userId);
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
