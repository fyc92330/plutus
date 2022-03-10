package org.chun.plutus.api.facade;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.chun.plutus.api.helper.LineMessageHelper;
import org.chun.plutus.api.mod.ActivityMod;
import org.chun.plutus.api.mod.MessageMod;
import org.chun.plutus.common.dao.ActivityBasicDao;
import org.chun.plutus.common.dao.ActivitySetDao;
import org.chun.plutus.common.dao.AppUserDao;
import org.chun.plutus.common.dto.JoinCodeDto;
import org.chun.plutus.common.enums.JoinCodeEnum;
import org.chun.plutus.common.exceptions.ActivityClosedException;
import org.chun.plutus.common.exceptions.ActivityDifferentException;
import org.chun.plutus.common.exceptions.ActivityNotFoundException;
import org.chun.plutus.common.exceptions.MultiActivityException;
import org.chun.plutus.common.exceptions.UserNotHostException;
import org.chun.plutus.common.exceptions.UserWithoutActivityException;
import org.chun.plutus.common.vo.ActivityBasicVo;
import org.chun.plutus.common.vo.AppUserVo;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

import static org.chun.plutus.common.constant.LineCommonMessageConst.ACTIVITY_CLOSED;
import static org.chun.plutus.common.constant.LineCommonMessageConst.ACTIVITY_DIFFERENT;
import static org.chun.plutus.common.constant.LineCommonMessageConst.ACTIVITY_NOT_FOUND;
import static org.chun.plutus.common.constant.LineCommonMessageConst.USER_NOT_HOST;
import static org.chun.plutus.common.constant.LineCommonMessageConst.WITHOUT_ACTIVITY;

@Slf4j
@RequiredArgsConstructor
@Service
public class MessageFacade {

  private final MessageMod messageMod;
  private final ActivityMod activityMod;
  private final AppUserDao appUserDao;
  private final ActivityBasicDao activityBasicDao;
  private final ActivitySetDao activitySetDao;
  private final LineMessageHelper lineMessageHelper;

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
    Arrays.stream(JoinCodeEnum.values())
        .map(JoinCodeEnum::val)
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
    final JoinCodeEnum joinCodeEnum = JoinCodeEnum.getEnum(prefix);
    final String joinCode = Optional.ofNullable(event.getMessage())
        .map(TextMessageContent::getText)
        .map(text -> text.replaceFirst(prefix, Strings.EMPTY))
        .orElse(Strings.EMPTY);
    final JoinCodeDto joinCodeDto = new JoinCodeDto(event.getReplyToken(), event.getSource().getUserId(), joinCode, userNum);
    String errorMsg = null;
    try {
      switch (joinCodeEnum) {
        case CREATE:
          createEvent(joinCodeDto);
          break;
        case VIEW:
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
          break;
        case SUB_TYPE:
          break;
        case SUB_COST:
          break;
        case SUB_PAYER:
          break;
        case FORCE_CREATE:
          forceCreateEvent(joinCodeDto);
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
    } finally {
      if (errorMsg != null) lineMessageHelper.sendErrorMessage(joinCodeDto, errorMsg);
    }
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
    final String joinCode = joinCodeDto.getJoinCode();
    activityMod.validActivityExists(joinCode);
    activityMod.validActivitySetExists(joinCode, joinCodeDto.getUserNum());
    activityMod.leaveActivityByJoinCode(joinCodeDto);
  }

  /**
   * 關閉活動
   *
   * @param joinCodeDto
   */
  private void closeEvent(JoinCodeDto joinCodeDto) {
    activityMod.validUserIsHost(joinCodeDto.getUserNum(), joinCodeDto.getJoinCode());
  }

}
