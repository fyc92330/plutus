package org.chun.plutus.api.facade;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.chun.plutus.api.helper.ImageUploadHelper;
import org.chun.plutus.api.helper.LineMessageHelper;
import org.chun.plutus.api.helper.LineRichMenuHelper;
import org.chun.plutus.api.mod.ActivityMod;
import org.chun.plutus.common.dto.LineUserDto;
import org.chun.plutus.common.enums.MenuEnum;
import org.chun.plutus.common.exceptions.ActivityClosedException;
import org.chun.plutus.common.exceptions.ActivityDifferentException;
import org.chun.plutus.common.exceptions.ActivityDtNotFoundException;
import org.chun.plutus.common.exceptions.ActivityNotFoundException;
import org.chun.plutus.common.exceptions.CustomValueEmptyException;
import org.chun.plutus.common.exceptions.FunctionNotSupportException;
import org.chun.plutus.common.exceptions.HostLeavingException;
import org.chun.plutus.common.exceptions.PayTypeChangeAlreadyException;
import org.chun.plutus.common.exceptions.UserNotHostException;
import org.chun.plutus.common.exceptions.UserWithoutActivityException;
import org.chun.plutus.common.vo.ActivityBasicVo;
import org.chun.plutus.common.vo.ActivityDtVo;
import org.springframework.stereotype.Service;

import java.util.Arrays;

import static org.chun.plutus.common.constant.LineCommonMessageConst.ACTIVITY_CLOSED;
import static org.chun.plutus.common.constant.LineCommonMessageConst.ACTIVITY_DIFFERENT;
import static org.chun.plutus.common.constant.LineCommonMessageConst.ACTIVITY_NOT_FOUND;
import static org.chun.plutus.common.constant.LineCommonMessageConst.ACT_DT_IS_NOT_FOUND;
import static org.chun.plutus.common.constant.LineCommonMessageConst.FUNCTION_NOT_SUPPORT;
import static org.chun.plutus.common.constant.LineCommonMessageConst.HOST_CANNOT_LEAVE;
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
   * 處理訊息事件 (文字訊息)
   *
   * @param event
   * @param lineUserDto
   */
  public void handleMessageEvent(MessageEvent event, LineUserDto lineUserDto) {
    MessageContent message = event.getMessage();
    if (message instanceof TextMessageContent) {
      final String text = ((TextMessageContent) message).getText();
      final String action = Arrays.stream(MenuEnum.Action.values())
          .map(MenuEnum.Action::val)
          .filter(text::startsWith)
          .findAny()
          .orElse(Strings.EMPTY);
      if (StringUtils.isNotBlank(action)) lineUserDto.setJoinCode(text.replace(action, Strings.EMPTY));
      final MenuEnum.Action actionEnum = MenuEnum.Action.getEnum(action);
      this.handleJoinCodeText(event, actionEnum, lineUserDto);
    } else {
      throw new FunctionNotSupportException();
    }
  }

  /**
   * 處理純文字訊息打進來的邀請碼
   *
   * @param event
   * @param actionEnum
   * @param lineUserDto
   */
  public void handleJoinCodeText(MessageEvent<TextMessageContent> event, MenuEnum.Action actionEnum, LineUserDto lineUserDto) {
    String errorMsg = null;
    try {
      switch (actionEnum) {
//        case CREATE:
//          createEvent(lineUserDto);
//          break;
//        case VIEW:
//          viewEvent(lineUserDto);
//          break;
        case INVITE:// v2實作
          break;
        case JOIN:
          joinEvent(lineUserDto);
          break;
//        case LEAVE:
//          leaveEvent(lineUserDto);
//          break;
        case CANCEL:// v2實作
          break;
//        case CLOSE:
//          closeEvent(lineUserDto);
//          break;
//        case NODE:
//          nodeEvent(lineUserDto);
//          break;
        case MENU:
          menuEvent(lineUserDto);
          break;
//        case FORCE_CREATE:
//          forceCreateEvent(lineUserDto);
//        case MAIN_MENU:
//        case SUB_MENU:
//          richMenuEvent(actionEnum, lineUserDto.getUserId());
//          break;
//        case QRCODE:
//          qrcodeEvent(lineUserDto);
//          break;
//        case CALL_MENU:
//          settingMenuEvent(lineUserDto);
//          break;
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
    } catch (ActivityDtNotFoundException afe) {
      errorMsg = ACT_DT_IS_NOT_FOUND;
    } catch (Exception e) {
      log.error("", e);
    } finally {
      if (errorMsg != null) lineMessageHelper.sendErrorMessage(lineUserDto, errorMsg);
    }
  }

  /**
   * 設定菜單的選項 (v2 remove)
   *
   * @param lineUserDto
   */
  public void menuEvent(LineUserDto lineUserDto) {
    final String commandCode = lineUserDto.getJoinCode();
    Arrays.stream(MenuEnum.Setting.values())
        .map(MenuEnum.Setting::val)
        .filter(commandCode::startsWith)
        .findAny()
        .ifPresent(action -> {
          activityMod.validUserActivityExists(lineUserDto.getUserNum());
          final String commandValue = commandCode.replace(action, Strings.EMPTY).trim();
          MenuEnum.Setting settingEnum = MenuEnum.Setting.getEnum(action);
          // 取得這次節點的資訊
          ActivityDtVo activityDtVo = activityMod.getUserCurrentActivityNode(lineUserDto.getUserNum());
          if (activityDtVo == null) throw new ActivityDtNotFoundException();
          activityMod.validSubMenuAction(settingEnum, commandValue, activityDtVo.getPayType());
          final String message = activityMod.setNodeWithCustomValue(settingEnum, commandValue, activityDtVo.getAcdNum());
          lineMessageHelper.sendTextMessage(lineUserDto, message);
        });
  }

  /** =================================================== private ================================================== */

//  /**
//   * 主辦人建立活動
//   *
//   * @param lineUserDto
//   */
//  private void createEvent(LineUserDto lineUserDto) {
//    try {
//      activityMod.validMultiActivity(lineUserDto.getUserNum());
//      this.forceCreateEvent(lineUserDto);
//    } catch (MultiActivityException e) {
//      lineMessageHelper.sendConfirmCreateMessage(lineUserDto);
//    }
//  }
//
//  /**
//   * 主辦人直接建立活動
//   *
//   * @param lineUserDto
//   */
//  private void forceCreateEvent(LineUserDto lineUserDto) {
//    final String joinCode = activityMod.forceCreateActivity(lineUserDto.getUserNum());
//    lineUserDto.setJoinCode(joinCode);
//    lineMessageHelper.sendCreateSuccessMessage(lineUserDto);
//  }

  /**
   * 加入活動
   *
   * @param lineUserDto
   */
  private void joinEvent(LineUserDto lineUserDto) {
    final String joinCode = lineUserDto.getJoinCode();
    activityMod.validActivityExists(joinCode);
    final ActivityBasicVo activityBasicVo = activityMod.joinActivityByJoinCode(lineUserDto.getUserNum(), joinCode);
    lineMessageHelper.sendJoinSuccessMessage(lineUserDto, activityBasicVo);
  }

//  /**
//   * 離開活動
//   *
//   * @param lineUserDto
//   */
//  private void leaveEvent(LineUserDto lineUserDto) {
//    final String joinCode = this.confirmCurrentActivity(lineUserDto);
//    lineUserDto.setJoinCode(joinCode);
//    activityMod.validActivityExists(joinCode);
//    activityMod.validActivitySetExists(joinCode, lineUserDto.getUserNum(), true);
//    activityMod.leaveActivityByJoinCode(lineUserDto);
//  }

//  /**
//   * 關閉活動
//   *
//   * @param lineUserDto
//   */
//  private void closeEvent(LineUserDto lineUserDto) {
//    final String joinCode = this.confirmCurrentActivity(lineUserDto);
//    lineUserDto.setJoinCode(joinCode);
//    activityMod.validUserIsHost(lineUserDto.getUserNum(), joinCode);
//    activityMod.closeActivityByJoinCode(lineUserDto);
//    lineMessageHelper.sendTextMessage(lineUserDto, CLOSE_SUCCESS);
//  }

//  /**
//   * 建立活動節點
//   *
//   * @param lineUserDto
//   */
//  private void nodeEvent(LineUserDto lineUserDto) {
//    final String joinCode = this.confirmCurrentActivity(lineUserDto);
//    lineUserDto.setJoinCode(joinCode);
//    activityMod.validActivityExists(joinCode);
//    activityMod.saveActivityNode(lineUserDto);
//    lineMessageHelper.sendTextMessage(lineUserDto, NODE_CREATE_SUCCESS);
//  }

//  /**
//   * 檢視活動
//   */
//  private void viewEvent(LineUserDto lineUserDto) {
//    final Long userNum = lineUserDto.getUserNum();
//    final String joinCode = this.confirmCurrentActivity(lineUserDto);
//    lineUserDto.setJoinCode(joinCode);
//    activityMod.validActivityExists(joinCode);
//    activityMod.validActivitySetExists(joinCode, userNum, false);
//    final String view = activityMod.sendActivityViewDistinguishRole(joinCode, userNum);
//    lineMessageHelper.sendTextMessage(lineUserDto, view);
//  }

//  /**
//   * 更換選單
//   *
//   * @param actionEnum
//   * @param userId
//   */
//  private void richMenuEvent(MenuEnum.Action actionEnum, String userId) {
//    if (actionEnum == MenuEnum.Action.SUB_MENU) {
//      lineRichMenuHelper.subMenuOnChange(userId);
//    } else if (actionEnum == MenuEnum.Action.MAIN_MENU) {
//      lineRichMenuHelper.mainMenuOnChange(userId);
//    }
//  }

//  /**
//   * 建立QRcode, 上傳暫存, 發送圖片
//   *
//   * @param lineUserDto
//   */
//  private void qrcodeEvent(LineUserDto lineUserDto) {
//    final String joinCode = this.confirmCurrentActivity(lineUserDto);
//    final QrcodeUrlDto qrcodeUrlDto = activityMod.getQrcodeUrlByJoinCode(joinCode);
//    final String qrcodeUrl = qrcodeUrlDto.getQrcodeUrl();
//    String imageUrl;
//    if (imageUploadHelper.qrcodeExists(qrcodeUrl)) {
//      log.info("路徑正確");
//      imageUrl = qrcodeUrl;
//    } else {
//      log.info("建立新的qrcode路徑");
//      // 建立新的qrcode上傳
//      final String url = QRCODE_INVITE_URL.concat(JoinCodeUtil.genJoinCode(joinCode));
//      ByteArrayOutputStream os = QrcodeUtil.generateQrcode(url);
//      imageUrl = imageUploadHelper.uploadImage(os).getUrl();
//      // 將路徑寫進活動
//      activityMod.saveQrcodeUrlInActivityVo(qrcodeUrlDto.getActNum(), imageUrl);
//    }
//
//    // 發送圖片訊息
//    lineMessageHelper.sendImageMessage(lineUserDto, imageUrl);
//  }

//  /**
//   * 設定子活動選單
//   *
//   * @param lineUserDto
//   */
//  private void settingMenuEvent(LineUserDto lineUserDto) {
//    activityMod.validActivityDtExists(lineUserDto.getUserNum());
//    final String joinCode = this.confirmCurrentActivity(lineUserDto);
//    lineUserDto.setJoinCode(joinCode);
//    final SubMenuImageDto subMenuImageDto = imageUploadHelper.genSubMenuDto(joinCode);
//    lineMessageHelper.sendMenuTemplateMessage(lineUserDto, subMenuImageDto);
//  }

  /**
   * 抓取參加的活動邀請碼(v1)
   *
   * @param lineUserDto
   * @return
   */
  private String confirmCurrentActivity(LineUserDto lineUserDto) {
    final String joinCode = activityMod.getJoinCodeBySetUserNum(lineUserDto.getUserNum());
    if (joinCode == null) throw new UserWithoutActivityException();
    return joinCode;
  }


}
