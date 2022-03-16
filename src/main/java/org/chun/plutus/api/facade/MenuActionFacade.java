package org.chun.plutus.api.facade;

import com.linecorp.bot.model.event.PostbackEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chun.plutus.api.helper.ImageUploadHelper;
import org.chun.plutus.api.helper.LineMessageHelper;
import org.chun.plutus.api.helper.LineRichMenuHelper;
import org.chun.plutus.api.mod.ActivityMod;
import org.chun.plutus.common.dto.LineUserDto;
import org.chun.plutus.common.dto.QrcodeUrlDto;
import org.chun.plutus.common.dto.SubMenuImageDto;
import org.chun.plutus.common.enums.MenuEnum;
import org.chun.plutus.common.exceptions.ActivityClosedException;
import org.chun.plutus.common.exceptions.ActivityDifferentException;
import org.chun.plutus.common.exceptions.ActivityDtNotFoundException;
import org.chun.plutus.common.exceptions.ActivityNotFoundException;
import org.chun.plutus.common.exceptions.CustomValueEmptyException;
import org.chun.plutus.common.exceptions.EmptyPartnerException;
import org.chun.plutus.common.exceptions.FunctionNotSupportException;
import org.chun.plutus.common.exceptions.HostLeavingException;
import org.chun.plutus.common.exceptions.MultiActivityException;
import org.chun.plutus.common.exceptions.PayTypeChangeAlreadyException;
import org.chun.plutus.common.exceptions.UserNotHostException;
import org.chun.plutus.common.exceptions.UserWithoutActivityException;
import org.chun.plutus.util.JoinCodeUtil;
import org.chun.plutus.util.QrcodeUtil;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

import static org.chun.plutus.common.constant.LineChannelViewConst.QRCODE_INVITE_URL;
import static org.chun.plutus.common.constant.LineCommonMessageConst.ACTIVITY_CLOSED;
import static org.chun.plutus.common.constant.LineCommonMessageConst.ACTIVITY_DIFFERENT;
import static org.chun.plutus.common.constant.LineCommonMessageConst.ACTIVITY_NOT_FOUND;
import static org.chun.plutus.common.constant.LineCommonMessageConst.ACT_DT_IS_NOT_FOUND;
import static org.chun.plutus.common.constant.LineCommonMessageConst.CLOSE_SUCCESS;
import static org.chun.plutus.common.constant.LineCommonMessageConst.FUNCTION_NOT_SUPPORT;
import static org.chun.plutus.common.constant.LineCommonMessageConst.HOST_CANNOT_LEAVE;
import static org.chun.plutus.common.constant.LineCommonMessageConst.NODE_CREATE_SUCCESS;
import static org.chun.plutus.common.constant.LineCommonMessageConst.PARTNER_EMPTY;
import static org.chun.plutus.common.constant.LineCommonMessageConst.PAY_TYPE_SETTING_ALREADY;
import static org.chun.plutus.common.constant.LineCommonMessageConst.SETTING_VALUE_EMPTY;
import static org.chun.plutus.common.constant.LineCommonMessageConst.USER_NOT_HOST;
import static org.chun.plutus.common.constant.LineCommonMessageConst.WITHOUT_ACTIVITY;
import static org.chun.plutus.common.enums.MenuEnum.Action.CREATE;
import static org.chun.plutus.common.enums.MenuEnum.Action.FORCE_CREATE;
import static org.chun.plutus.common.enums.MenuEnum.Action.MAIN_MENU;
import static org.chun.plutus.common.enums.MenuEnum.Action.SUB_MENU;

@Slf4j
@RequiredArgsConstructor
@Service
public class MenuActionFacade {

  private static final List<MenuEnum.Action> unauthActionList = Arrays.asList(MAIN_MENU, SUB_MENU, CREATE, FORCE_CREATE);

  private final ActivityMod activityMod;
  private final LineRichMenuHelper lineRichMenuHelper;
  private final LineMessageHelper lineMessageHelper;
  private final ImageUploadHelper imageUploadHelper;

  /**
   * 處理postback事件
   *
   * @param event
   * @param lineUserDto
   */
  public void handlePostbackEvent(PostbackEvent event, LineUserDto lineUserDto) {
    final String data = event.getPostbackContent().getData();
    final MenuEnum.Action actionEnum = MenuEnum.Action.getEnum(data);
    String errorMsg = null;
    try {
      // 取得活動邀請碼
      final String joinCode = activityMod.getJoinCodeBySetUserNum(lineUserDto.getUserNum());
      if (!unauthActionList.contains(actionEnum) && joinCode == null) throw new UserWithoutActivityException();
      lineUserDto.setJoinCode(joinCode);
      switch (actionEnum) {
        case MAIN_MENU:
        case SUB_MENU:
          richMenuEvent(actionEnum, lineUserDto.getUserId());
          break;
        case VIEW:
          viewEvent(lineUserDto);
          break;
        case LEAVE:
          leaveEvent(lineUserDto);
          break;
        case CREATE:
          createEvent(lineUserDto);
          break;
        case FORCE_CREATE:
          forceCreateEvent(lineUserDto);
          break;
        case NODE:
          nodeEvent(lineUserDto);
          break;
        case CALL_MENU:
          settingMenuEvent(lineUserDto);
          break;
        case CLOSE:
          closeEvent(lineUserDto);
          break;
        case QRCODE:
          qrcodeEvent(lineUserDto);
          break;
      }
    } catch (UserWithoutActivityException ua) {
      errorMsg = WITHOUT_ACTIVITY;
    } catch (ActivityNotFoundException ae) {
      errorMsg = ACTIVITY_NOT_FOUND;
    } catch (ActivityClosedException ace) {
      errorMsg = ACTIVITY_CLOSED;
    } catch (ActivityDifferentException ad) {
      errorMsg = ACTIVITY_DIFFERENT;
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
    } catch (EmptyPartnerException ep) {
      errorMsg = PARTNER_EMPTY;
    } catch (Exception e) {
      log.error("", e);
    } finally {
      if (errorMsg != null) lineMessageHelper.sendErrorMessage(lineUserDto, errorMsg);
    }
  }

  /** =================================================== private ================================================== */

  /**
   * 更換選單
   *
   * @param actionEnum
   * @param userId
   */
  private void richMenuEvent(MenuEnum.Action actionEnum, String userId) {
    if (actionEnum == SUB_MENU) {
      lineRichMenuHelper.subMenuOnChange(userId);
    } else if (actionEnum == MAIN_MENU) {
      lineRichMenuHelper.mainMenuOnChange(userId);
    }
  }

  /**
   * 檢視活動
   *
   * @param lineUserDto
   */
  private void viewEvent(LineUserDto lineUserDto) {
    final Long userNum = lineUserDto.getUserNum();
    final String joinCode = lineUserDto.getJoinCode();
    activityMod.validActivityExists(joinCode);
    activityMod.validActivitySetExists(joinCode, userNum, false);
    final String view = activityMod.sendActivityViewDistinguishRole(joinCode, userNum);
    lineMessageHelper.sendTextMessage(lineUserDto, view);
  }

  /**
   * 離開活動
   *
   * @param lineUserDto
   */
  private void leaveEvent(LineUserDto lineUserDto) {
    final String joinCode = lineUserDto.getJoinCode();
    activityMod.validActivityExists(joinCode);
    activityMod.validActivitySetExists(joinCode, lineUserDto.getUserNum(), true);
    activityMod.leaveActivityByJoinCode(lineUserDto);
  }

  /**
   * 主辦人建立活動
   *
   * @param lineUserDto
   */
  private void createEvent(LineUserDto lineUserDto) {
    try {
      activityMod.validMultiActivity(lineUserDto.getUserNum());
      this.forceCreateEvent(lineUserDto);
    } catch (MultiActivityException e) {
      lineMessageHelper.sendConfirmCreateMessage(lineUserDto);
    }
  }

  /**
   * 主辦人直接建立活動
   *
   * @param lineUserDto
   */
  private void forceCreateEvent(LineUserDto lineUserDto) {
    final String joinCode = activityMod.forceCreateActivity(lineUserDto.getUserNum());
    lineUserDto.setJoinCode(joinCode);
    lineMessageHelper.sendCreateSuccessMessage(lineUserDto);
  }

  /**
   * 建立活動節點
   *
   * @param lineUserDto
   */
  private void nodeEvent(LineUserDto lineUserDto) {
    activityMod.validActivityExists(lineUserDto.getJoinCode());
    activityMod.saveActivityNode(lineUserDto);
    lineMessageHelper.sendTextMessage(lineUserDto, NODE_CREATE_SUCCESS);
  }

  /**
   * 設定子活動選單
   *
   * @param lineUserDto
   */
  private void settingMenuEvent(LineUserDto lineUserDto) {
    activityMod.validActivityDtExists(lineUserDto);
    final SubMenuImageDto subMenuImageDto = imageUploadHelper.genSubMenuDto(lineUserDto.getJoinCode());
    lineMessageHelper.sendMenuTemplateMessage(lineUserDto, subMenuImageDto);
  }

  /**
   * 關閉活動
   *
   * @param lineUserDto
   */
  private void closeEvent(LineUserDto lineUserDto) {
    activityMod.validUserIsHost(lineUserDto.getUserNum(), lineUserDto.getJoinCode());
    activityMod.closeActivityByJoinCode(lineUserDto);
    lineMessageHelper.sendTextMessage(lineUserDto, CLOSE_SUCCESS);
  }

  /**
   * 建立QRcode, 上傳暫存, 發送圖片
   *
   * @param lineUserDto
   */
  private void qrcodeEvent(LineUserDto lineUserDto) {
    final String joinCode = lineUserDto.getJoinCode();
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
      imageUrl = imageUploadHelper.uploadImage(os).getUrl();
      // 將路徑寫進活動
      activityMod.saveQrcodeUrlInActivityVo(qrcodeUrlDto.getActNum(), imageUrl);
    }

    // 發送圖片訊息
    lineMessageHelper.sendImageMessage(lineUserDto, imageUrl);
  }
}
