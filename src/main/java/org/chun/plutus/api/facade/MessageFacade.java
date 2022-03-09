package org.chun.plutus.api.facade;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.chun.plutus.api.helper.LineMessageHelper;
import org.chun.plutus.api.mod.ActivityMod;
import org.chun.plutus.api.mod.JoinCodeActionMod;
import org.chun.plutus.api.mod.MessageMod;
import org.chun.plutus.common.dao.ActivityBasicDao;
import org.chun.plutus.common.dao.ActivitySetDao;
import org.chun.plutus.common.dao.AppUserDao;
import org.chun.plutus.common.dto.JoinCodeDto;
import org.chun.plutus.common.enums.ActivityEnum;
import org.chun.plutus.common.enums.JoinCodeEnum;
import org.chun.plutus.common.exceptions.MultiActivityException;
import org.chun.plutus.common.vo.ActivityBasicVo;
import org.chun.plutus.common.vo.AppUserVo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.chun.plutus.util.MomentUtil.DateTime.yyyy_MM_dd_HH_mm_ss;

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
    final String joinCode = joinCodeEnum == JoinCodeEnum.CREATE
        ? genActivity(userNum)
        : Optional.ofNullable(event.getMessage())
        .map(TextMessageContent::getText)
        .map(text -> text.replaceFirst(prefix, Strings.EMPTY))
        .orElse(Strings.EMPTY);
    final JoinCodeDto joinCodeDto = new JoinCodeDto(event.getReplyToken(), event.getSource().getUserId(), joinCode, userNum);
    switch (joinCodeEnum) {
      case CREATE:
        this.createEvent(joinCodeDto);
        break;
      case VIEW:
        break;
      case INVITE:// v2實作
        break;
      case JOIN:
        break;
      case LEAVE:
        break;
      case CANCEL:// v2實作
        break;
      case CLOSE:
        break;
      case SUB_CREATE:
        break;
      case SUB_CLOSE:
        break;
      case SUB_TYPE:
        break;
      case SUB_COST:
        break;
      case SUB_PAYER:
        break;
      case FORCE_CREATE:
        this.forceCreateEvent(joinCodeDto);
        break;
    }
  }

  /** =================================================== private ================================================== */

  /**
   * 主辦人建立活動
   *
   * @param joinCodeDto
   */
  private void createEvent(JoinCodeDto joinCodeDto) {
    try{
      activityMod.validMultiActivity(joinCodeDto.getUserNum());
    }catch (MultiActivityException e){
      lineMessageHelper.sendConfirmCreateMessage(joinCodeDto);
    }
    this.forceCreateEvent(joinCodeDto);
  }

  /**
   * 主辦人直接建立活動
   *
   * @param joinCodeDto
   */
  private void forceCreateEvent(JoinCodeDto joinCodeDto){
    activityMod.forceCreateActivity(joinCodeDto.getUserNum(), joinCodeDto.getJoinCode());
    lineMessageHelper.sendCreateSuccessMessage(joinCodeDto);
  }


  /**
   * 組裝活動並建立
   *
   * @param userNum
   * @return
   */
  private String genActivity(Long userNum) {
    final String userName = Optional.ofNullable(appUserDao.getByPk(userNum))
        .map(AppUserVo::getUserName)
        .orElseThrow(RuntimeException::new);
    final String now = yyyy_MM_dd_HH_mm_ss.format(LocalDateTime.now());
    ActivityBasicVo activityBasicVo = new ActivityBasicVo();
    activityBasicVo.setUserNum(userNum);
    activityBasicVo.setActStatus(ActivityEnum.Status.PREPARE.val());
    activityBasicVo.setStartDate(now);
    activityBasicVo.setCreateDate(now);
    activityBasicVo.setActTitle(String.format("%s所發起的活動", userName));
    return activityMod.saveActivityFirstVersion(activityBasicVo);
  }
}
