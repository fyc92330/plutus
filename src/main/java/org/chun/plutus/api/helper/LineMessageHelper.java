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
import org.chun.plutus.util.CacheUtil;
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
  private final CacheUtil cacheUtil;

  /**
   * ????????????
   *
   * @param replyToken
   * @param userId
   */
  public void sendFirstWelcomeMessage(String replyToken, String userId) {
    replyMessage(new TextMessage(String.format(WELCOME_MESSAGE, APP_VERSION)), replyToken, userId);
  }

  /**
   * ?????????????????????, ????????????
   *
   * @param lineUserDto
   */
  public void sendConfirmCreateMessage(LineUserDto lineUserDto, String captcha) {

    cacheUtil.putIntoCache("CaptchaCache_60", captcha, captcha);

    ConfirmTemplate template = new ConfirmTemplate(CONFIRM_CREATE, Arrays.asList(
        new PostbackAction(YES_STR, FORCE_CREATE.val().concat(captcha)),
        new PostbackAction(NO_STR, CANCEL.val().concat(captcha))
    ));

    sendTemplateMessage(lineUserDto, new TemplateMessage("???????????????", template));
  }

  /**
   * ??????????????????????????????
   *
   * @param lineUserDto
   */
  public void sendCreateSuccessMessage(LineUserDto lineUserDto) {
    TextMessage textMessage = new TextMessage(String.format(CREATE_SUCCESS, lineUserDto.getJoinCode()));
    replyMessage(textMessage, lineUserDto.getReplyToken(), lineUserDto.getUserId());
  }

  /**
   * ??????????????????
   *
   * @param lineUserDto
   * @param errorMsg
   */
  public void sendErrorMessage(LineUserDto lineUserDto, String errorMsg) {
    this.sendTextMessage(lineUserDto, errorMsg);
  }

  /**
   * ????????????????????????
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
   * ????????????????????????
   *
   * @param lineUserDto
   * @param subMenuImageDto
   * @return
   */
  public void sendMenuTemplateMessage(LineUserDto lineUserDto, SubMenuImageDto subMenuImageDto) {
    // ???????????????????????????
    ImageCarouselTemplate imageCarouselTemplate = generateSettingTemplateMessage(subMenuImageDto);
    TemplateMessage settingTemplate = TemplateMessage.builder()
        .altText("????????????")
        .template(imageCarouselTemplate)
        .build();

    // ????????????????????????
    CarouselColumn carouselColumn = CarouselColumn.builder()
        .thumbnailImageUrl(URI.create(subMenuImageDto.getTypeImageUrl()))
        .imageBackgroundColor("#FFFFFF")
        .title("??????????????????")
        .text("??????????????????")
        .actions(this.genPayTypeActions())
        .build();

    // ????????????????????????
    CarouselTemplate carouselTemplate = CarouselTemplate.builder()
        .imageAspectRatio("square")
        .imageSize("contain")
        .columns(Collections.singletonList(carouselColumn))
        .build();
    TemplateMessage payTypeTemplate = TemplateMessage.builder()
        .altText("????????????")
        .template(carouselTemplate)
        .build();

    this.sendTemplateMessage(lineUserDto, settingTemplate, payTypeTemplate);
  }

  /** =================================================== Message ================================================== */

  /**
   * ??????????????????
   *
   * @param lineUserDto
   * @param textContent
   */
  public void sendTextMessage(LineUserDto lineUserDto, String textContent) {
    replyMessage(new TextMessage(textContent), lineUserDto.getReplyToken(), lineUserDto.getUserId());

  }

  /**
   * ??????????????????
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
   * ??????????????????
   *
   * @param lineUserDto
   * @param templateMessages
   */
  public void sendTemplateMessage(LineUserDto lineUserDto, TemplateMessage... templateMessages) {
    replyMessages(Arrays.asList(templateMessages), lineUserDto.getReplyToken(), lineUserDto.getUserId());
  }

  /** =================================================== private ================================================== */

  /**
   * ??????????????????????????????
   *
   * @param subMenuImageDto
   * @return
   */
  private ImageCarouselTemplate generateSettingTemplateMessage(SubMenuImageDto subMenuImageDto) {

    List<ImageCarouselColumn> imageCarouselColumnList = new ArrayList<>();
    final String menu = MENU.val();
    // ????????????
    URIAction setTitleAction = new URIAction("??????????????????", URI.create(StringUtil.concat(QRCODE_INVITE_URL, menu, TITLE.val())), null);
    ImageCarouselColumn titleSetting = new ImageCarouselColumn(URI.create(subMenuImageDto.getTitleImageUrl()), setTitleAction);
    imageCarouselColumnList.add(titleSetting);

    // ??????????????????
    URIAction setCostAction = new URIAction("??????????????????", URI.create(StringUtil.concat(QRCODE_INVITE_URL, menu, COST.val())), null);
    ImageCarouselColumn castSetting = new ImageCarouselColumn(URI.create(subMenuImageDto.getCostImageUrl()), setCostAction);
    imageCarouselColumnList.add(castSetting);

    // ??????????????????
    URIAction setPayerAction = new URIAction("???????????????", URI.create(StringUtil.concat(QRCODE_INVITE_URL, menu, PAYER.val())), null);
    ImageCarouselColumn payerSetting = new ImageCarouselColumn(URI.create(subMenuImageDto.getDaddyImageUrl()), setPayerAction);
    imageCarouselColumnList.add(payerSetting);

    return new ImageCarouselTemplate(imageCarouselColumnList);
  }

  /**
   * ?????????????????????????????????
   *
   * @return
   */
  private List<Action> genPayTypeActions() {
    final String menu = MENU.val();
    return Arrays.asList(
        new URIAction("????????????", URI.create(StringUtil.concat(QRCODE_INVITE_URL, menu, TYPE_AVERAGE.val())), null),
        new URIAction("???????????????", URI.create(StringUtil.concat(QRCODE_INVITE_URL, menu, TYPE_SCALE.val())), null),
        new URIAction("????????????", URI.create(StringUtil.concat(QRCODE_INVITE_URL, menu, TYPE_CHOICE.val())), null)
    );
  }

  /**
   * ????????????, ????????????push???????????????
   *
   * @param message
   * @param replyToken
   * @param userId
   */
  public void replyMessage(Message message, String replyToken, String userId) {
    lineBotService.reply(new ReplyMessage(replyToken, message), userId);
  }

  private void replyMessages(List<Message> messages, String replyToken, String userId) {
    lineBotService.reply(new ReplyMessage(replyToken, messages), userId);
  }

  /**
   * ????????????
   *
   * @param message
   * @param userId
   */
  private void pushMessage(Message message, String userId) {
    lineBotService.push(new PushMessage(userId, message));
  }

}
