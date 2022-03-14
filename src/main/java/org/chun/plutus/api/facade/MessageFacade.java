package org.chun.plutus.api.facade;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.chun.plutus.api.helper.ImageUploadHelper;
import org.chun.plutus.api.helper.LineMessageHelper;
import org.chun.plutus.api.helper.LineRichMenuHelper;
import org.chun.plutus.api.mod.ActivityMod;
import org.chun.plutus.common.dto.JoinCodeDto;
import org.chun.plutus.common.dto.QrcodeUrlDto;
import org.chun.plutus.common.enums.JoinCodeEnum;
import org.chun.plutus.common.exceptions.ActivityClosedException;
import org.chun.plutus.common.exceptions.ActivityDifferentException;
import org.chun.plutus.common.exceptions.ActivityNotFoundException;
import org.chun.plutus.common.exceptions.CustomValueEmptyException;
import org.chun.plutus.common.exceptions.FunctionNotSupportException;
import org.chun.plutus.common.exceptions.HostLeavingException;
import org.chun.plutus.common.exceptions.MultiActivityException;
import org.chun.plutus.common.exceptions.PayTypeChangeAlreadyException;
import org.chun.plutus.common.exceptions.UserNotHostException;
import org.chun.plutus.common.exceptions.UserWithoutActivityException;
import org.chun.plutus.common.vo.ActivityBasicVo;
import org.chun.plutus.common.vo.ActivityDtVo;
import org.chun.plutus.common.vo.AppUserVo;
import org.chun.plutus.util.JoinCodeUtil;
import org.chun.plutus.util.QrcodeUtil;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Optional;

import static org.chun.plutus.common.constant.LineChannelViewConst.QRCODE_INVITE_URL;
import static org.chun.plutus.common.constant.LineCommonMessageConst.ACTIVITY_CLOSED;
import static org.chun.plutus.common.constant.LineCommonMessageConst.ACTIVITY_DIFFERENT;
import static org.chun.plutus.common.constant.LineCommonMessageConst.ACTIVITY_NOT_FOUND;
import static org.chun.plutus.common.constant.LineCommonMessageConst.CLOSE_SUCCESS;
import static org.chun.plutus.common.constant.LineCommonMessageConst.FUNCTION_NOT_SUPPORT;
import static org.chun.plutus.common.constant.LineCommonMessageConst.HOST_CANNOT_LEAVE;
import static org.chun.plutus.common.constant.LineCommonMessageConst.NODE_CREATE_SUCCESS;
import static org.chun.plutus.common.constant.LineCommonMessageConst.PAY_TYPE_SETTING_ALREADY;
import static org.chun.plutus.common.constant.LineCommonMessageConst.SETTING_VALUE_EMPTY;
import static org.chun.plutus.common.constant.LineCommonMessageConst.USER_NOT_HOST;
import static org.chun.plutus.common.constant.LineCommonMessageConst.WITHOUT_ACTIVITY;

@Slf4j
@RequiredArgsConstructor
@Service
public class MessageFacade {

  private final ActivityMod activityMod;
  private final LineMessageHelper lineMessageHelper;
  private final LineRichMenuHelper lineRichMenuHelper;
  private final ImageUploadHelper imageUploadHelper;


  /**
   * 處理訊息事件
   *
   * @param event
   * @param appUserVo
   */
  public void handleMessageEvent(MessageEvent event, AppUserVo appUserVo) {
    MessageContent message = event.getMessage();
    if (message instanceof TextMessageContent) {
      handleTextMessage(event, appUserVo.getUserNum());
    }
  }

  /**
   * 處理文字訊息
   *
   * @param event
   * @param userNum
   */
  private void handleTextMessage(MessageEvent<TextMessageContent> event, Long userNum) {
    final String text = event.getMessage().getText();
    Arrays.stream(JoinCodeEnum.Action.values())
        .map(JoinCodeEnum.Action::val)
        .filter(text::startsWith)
        .findAny()
        .ifPresent(prefix -> handleJoinCodeText(event, userNum, prefix));

    //todo v1.5 qrcode相關邏輯
  }

  /**
   * 處理純文字訊息打進來的邀請碼
   *
   * @param event
   * @param userNum
   * @param prefix
   */
  public void handleJoinCodeText(MessageEvent<TextMessageContent> event, Long userNum, String prefix) {
    final JoinCodeEnum.Action actionEnum = JoinCodeEnum.Action.getEnum(prefix);
    final String joinCode = Optional.ofNullable(event.getMessage())
        .map(TextMessageContent::getText)
        .map(text -> text.replace(prefix, Strings.EMPTY))
        .orElse(Strings.EMPTY);
    final String userId = event.getSource().getUserId();
    final JoinCodeDto joinCodeDto = new JoinCodeDto(event.getReplyToken(), userId, joinCode, userNum);
    String errorMsg = null;
    try {
      switch (actionEnum) {
        case CREATE:
          createEvent(joinCodeDto);
          break;
        case VIEW:
          viewEvent(joinCodeDto);
          break;
        case INVITE:// v2實作
          break;
        case JOIN:
          joinEvent(joinCodeDto);
          break;
        case LEAVE:
          leaveEvent(joinCodeDto);
          break;
        case CANCEL:// v2實作
          break;
        case CLOSE:
          closeEvent(joinCodeDto);
          break;
        case NODE:
          nodeEvent(joinCodeDto);
          break;
        case MENU:
          menuEvent(joinCodeDto);
          break;
        case FORCE_CREATE:
          forceCreateEvent(joinCodeDto);
        case MAIN_MENU:
        case SUB_MENU:
          richMenuEvent(actionEnum, userId);
          break;
        case QRCODE:
          qrcodeEvent(joinCodeDto);
          break;
      }
    } catch (ActivityNotFoundException ae) {
      errorMsg = ACTIVITY_NOT_FOUND;
    } catch (ActivityClosedException ace) {
      errorMsg = ACTIVITY_CLOSED;
    } catch (ActivityDifferentException ad) {
      errorMsg = ACTIVITY_DIFFERENT;
    } catch (UserWithoutActivityException ua) {
      errorMsg = WITHOUT_ACTIVITY;
    } catch (UserNotHostException uh) {
      errorMsg = USER_NOT_HOST;
    } catch (HostLeavingException hl) {
      errorMsg = HOST_CANNOT_LEAVE;
    } catch (PayTypeChangeAlreadyException pa) {
      errorMsg = PAY_TYPE_SETTING_ALREADY;
    } catch (CustomValueEmptyException ce) {
      errorMsg = SETTING_VALUE_EMPTY;
    } catch (FunctionNotSupportException fe) {
      errorMsg = FUNCTION_NOT_SUPPORT;
    } finally {
      if (errorMsg != null) lineMessageHelper.sendErrorMessage(joinCodeDto, errorMsg);
    }
  }

  /**
   * 設定菜單的選項 (v2 remove)
   *
   * @param joinCodeDto
   */
  public void menuEvent(JoinCodeDto joinCodeDto) {
    final String commandCode = joinCodeDto.getJoinCode();
    Arrays.stream(JoinCodeEnum.Menu.values())
        .map(JoinCodeEnum.Menu::val)
        .filter(commandCode::startsWith)
        .findAny()
        .ifPresent(action -> {
          activityMod.validUserActivityExists(joinCodeDto.getUserNum());
          final String commandValue = commandCode.replace(action, Strings.EMPTY).trim();
          JoinCodeEnum.Menu menuEnum = JoinCodeEnum.Menu.getEnum(action);
          // 取得這次節點的資訊
          ActivityDtVo activityDtVo = activityMod.getUserCurrentActivityNode(joinCodeDto.getUserNum());
          if (activityDtVo == null) return;
          activityMod.validSubMenuAction(menuEnum, commandValue, activityDtVo.getPayType());
          activityMod.setNodeWithCustomValue(menuEnum, commandValue, activityDtVo.getAcdNum());
        });
  }

  /** =================================================== private ================================================== */

  /**
   * 主辦人建立活動
   *
   * @param joinCodeDto
   */
  private void createEvent(JoinCodeDto joinCodeDto) {
    try {
      activityMod.validMultiActivity(joinCodeDto.getUserNum());
    } catch (MultiActivityException e) {
      log.info("too many activity exists.");
      lineMessageHelper.sendConfirmCreateMessage(joinCodeDto);
    }
    this.forceCreateEvent(joinCodeDto);
  }

  /**
   * 主辦人直接建立活動
   *
   * @param joinCodeDto
   */
  private void forceCreateEvent(JoinCodeDto joinCodeDto) {
    final String joinCode = activityMod.forceCreateActivity(joinCodeDto.getUserNum());
    joinCodeDto.setJoinCode(joinCode);
    lineMessageHelper.sendCreateSuccessMessage(joinCodeDto);
  }

  /**
   * 加入活動
   *
   * @param joinCodeDto
   */
  private void joinEvent(JoinCodeDto joinCodeDto) {
    final String joinCode = joinCodeDto.getJoinCode();
    activityMod.validActivityExists(joinCode);
    final ActivityBasicVo activityBasicVo = activityMod.joinActivityByJoinCode(joinCodeDto.getUserNum(), joinCode);
    lineMessageHelper.sendJoinSuccessMessage(joinCodeDto, activityBasicVo);
  }

  /**
   * 離開活動
   *
   * @param joinCodeDto
   */
  private void leaveEvent(JoinCodeDto joinCodeDto) {
    final String joinCode = this.confirmCurrentActivity(joinCodeDto);
    joinCodeDto.setJoinCode(joinCode);
    activityMod.validActivityExists(joinCode);
    activityMod.validActivitySetExists(joinCode, joinCodeDto.getUserNum(), true);
    activityMod.leaveActivityByJoinCode(joinCodeDto);
  }

  /**
   * 關閉活動
   *
   * @param joinCodeDto
   */
  private void closeEvent(JoinCodeDto joinCodeDto) {
    final String joinCode = this.confirmCurrentActivity(joinCodeDto);
    joinCodeDto.setJoinCode(joinCode);
    activityMod.validUserIsHost(joinCodeDto.getUserNum(), joinCode);
    activityMod.closeActivityByJoinCode(joinCodeDto);
    lineMessageHelper.sendTextMessage(joinCodeDto, CLOSE_SUCCESS);
  }

  /**
   * 建立活動節點
   *
   * @param joinCodeDto
   */
  private void nodeEvent(JoinCodeDto joinCodeDto) {
    final String joinCode = this.confirmCurrentActivity(joinCodeDto);
    joinCodeDto.setJoinCode(joinCode);
    activityMod.validActivityExists(joinCode);
    activityMod.saveActivityNode(joinCodeDto);
    lineMessageHelper.sendTextMessage(joinCodeDto, NODE_CREATE_SUCCESS);
  }

  /**
   * 檢視活動
   */
  private void viewEvent(JoinCodeDto joinCodeDto) {
    final Long userNum = joinCodeDto.getUserNum();
    final String joinCode = this.confirmCurrentActivity(joinCodeDto);
    joinCodeDto.setJoinCode(joinCode);
    activityMod.validActivityExists(joinCode);
    activityMod.validActivitySetExists(joinCode, userNum, false);
    final String view = activityMod.sendActivityViewDistinguishRole(joinCode, userNum);
    lineMessageHelper.sendTextMessage(joinCodeDto, view);
  }

  /**
   * 更換選單
   *
   * @param actionEnum
   * @param userId
   */
  private void richMenuEvent(JoinCodeEnum.Action actionEnum, String userId) {
    if (actionEnum == JoinCodeEnum.Action.SUB_MENU) {
      lineRichMenuHelper.subMenuOnChange(userId);
    } else if (actionEnum == JoinCodeEnum.Action.MAIN_MENU) {
      lineRichMenuHelper.mainMenuOnChange(userId);
    }
  }

  /**
   * 建立QRcode, 上傳暫存, 發送圖片
   *
   * @param joinCodeDto
   */
  private void qrcodeEvent(JoinCodeDto joinCodeDto) {
    final String joinCode = this.confirmCurrentActivity(joinCodeDto);
    final QrcodeUrlDto qrcodeUrlDto = activityMod.getQrcodeUrlByJoinCode(joinCode);
    final String qrcodeUrl = qrcodeUrlDto.getQrcodeUrl();
    String imageUrl;
    if (imageUploadHelper.qrcodeExists(qrcodeUrl)) {
      log.info("路徑正確");
      imageUrl = qrcodeUrl;
    } else {
      log.info("建立新的qrcode路徑");
      // 建立新的qrcode上傳
      final String url = QRCODE_INVITE_URL.concat(JoinCodeUtil.genJoinCode(joinCode));
      ByteArrayOutputStream os = QrcodeUtil.generateQrcode(url);
      imageUrl = imageUploadHelper.uploadImage(os);
      // 將路徑寫進活動
      activityMod.saveQrcodeUrlInActivityVo(qrcodeUrlDto.getActNum(), imageUrl);
    }

    // 發送圖片訊息
    lineMessageHelper.sendImageMessage(joinCodeDto, imageUrl);
  }

  /**
   * 抓取參加的活動邀請碼(v1)
   *
   * @param joinCodeDto
   * @return
   */
  private String confirmCurrentActivity(JoinCodeDto joinCodeDto) {
    final String joinCode = activityMod.getJoinCodeBySetUserNum(joinCodeDto.getUserNum());
    if (joinCode == null) throw new UserWithoutActivityException();
    return joinCode;
  }


}
