package org.chun.plutus.api.mod;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.util.Strings;
import org.chun.plutus.api.helper.LineMessageHelper;
import org.chun.plutus.common.dao.ActivityBasicDao;
import org.chun.plutus.common.dao.ActivitySetDao;
import org.chun.plutus.common.dao.AppUserDao;
import org.chun.plutus.common.enums.ActivityEnum;
import org.chun.plutus.common.enums.JoinCodePrefixEnum;
import org.chun.plutus.common.exceptions.ActivityInProgressException;
import org.chun.plutus.common.exceptions.ActivityNotFoundException;
import org.chun.plutus.common.exceptions.UserNotFoundException;
import org.chun.plutus.common.mo.InviteJoinCodeMo;
import org.chun.plutus.common.rvo.ActivityViewRvo;
import org.chun.plutus.common.rvo.CancelActivityRvo;
import org.chun.plutus.common.vo.ActivityBasicVo;
import org.chun.plutus.common.vo.ActivitySetVo;
import org.chun.plutus.common.vo.AppUserVo;
import org.chun.plutus.util.DaoValidationUtil;
import org.chun.plutus.util.MapUtil;
import org.chun.plutus.util.RequestScopeUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.chun.plutus.util.MomentUtil.DateTime.yyyy_MM_dd_HH_mm_ss;

@Slf4j
@RequiredArgsConstructor
@Service
public class ActivityMod {

  private final ActivityBasicDao activityBasicDao;
  private final ActivitySetDao activitySetDao;
  private final AppUserDao appUserDao;
  private final LineMessageHelper lineMessageHelper;

  /**
   * 建立一筆活動
   *
   * @param activityBasicVo
   */
  public Long saveActivity(ActivityBasicVo activityBasicVo) {
    final String nowDate = yyyy_MM_dd_HH_mm_ss.format(LocalDateTime.now());
    final String startDate = activityBasicVo.getStartDate();
    final Long userNum = RequestScopeUtil.getUserNum();
    activityBasicVo.setUserNum(userNum);
    activityBasicVo.setActStatus(ActivityEnum.Status.PREPARE.val());
    activityBasicVo.setStartDate(startDate == null ? nowDate : startDate);
    activityBasicVo.setCreateDate(nowDate);
    // 組裝邀請碼
    activityBasicVo.setJoinCode(RandomStringUtils.random(8, true, true).toLowerCase());
    activityBasicDao.insert(activityBasicVo);
    // 建立人員成為第一名參與者
    final Long actNum = activityBasicVo.getActNum();
    saveFirstActSet(userNum, actNum, nowDate);
    return actNum;
  }

  /**
   * 取得當前活動的資料
   *
   * @param userNum
   * @return
   */
  public ActivityViewRvo getCurrentActivity(Long userNum) {
    return activityBasicDao.getCurrentActivityView(userNum);
  }

  /**
   * 取得邀請名單
   *
   * @param userNum
   * @return
   */
  public List<AppUserVo> getUserListToInviteList(Long userNum) {
    return activitySetDao.listUserListByActivityHistory(userNum).stream()
        .filter(user -> !userNum.equals(user.getUserNum()))
        .collect(Collectors.toList());
  }

  /**
   * 取得現有的活動邀請碼
   *
   * @param userNumList
   * @return
   */
  public InviteJoinCodeMo getJoinCodeByCurrentActivity(List<Long> userNumList) {
    ActivityBasicVo activityBasicVo = activityBasicDao.getOwnerActivityInfo(RequestScopeUtil.getUserNum());
    final Long actNum = activityBasicVo.getActNum();
    // 邀請名單建立邀請物件
    userNumList.stream()
        .filter(num -> !Objects.equals(RequestScopeUtil.getUserNum(), num))
        .map(num -> {
          ActivitySetVo activitySetVo = new ActivitySetVo();
          activitySetVo.setUserNum(num);
          activitySetVo.setStatus(ActivityEnum.SetStatus.INVITE.val());
          activitySetVo.setStartDate(yyyy_MM_dd_HH_mm_ss.format(LocalDateTime.now()));
          activitySetVo.setActNum(actNum);
          return activitySetVo;
        })
        .forEach(activitySetDao::insert);
    // 回傳參加邀請碼
    return new InviteJoinCodeMo(
        activityBasicVo.getJoinCode(), activityBasicVo.getActTitle(), activityBasicVo.getHostUserName(), userNumList);
  }

  /**
   * 處理純文字訊息打進來的邀請碼
   *
   * @param event
   * @param userNum
   * @param prefix
   */
  public void handleJoinCodeText(MessageEvent<TextMessageContent> event, Long userNum, String prefix) {
    final JoinCodePrefixEnum joinCodePrefixEnum = JoinCodePrefixEnum.getEnum(prefix);
    final String joinCode = event.getMessage().getText().replaceFirst(prefix, Strings.EMPTY);
    switch (joinCodePrefixEnum) {
      case JOIN:
        joinActivitySetByJoinCode(joinCode, userNum, event);
        break;
      case LEAVE:
        leaveActivitySetByLeaveCode(joinCode, userNum, event);
        break;
      case INVITE:
        joinActivitySetByInviteCode(joinCode, userNum, event);
        break;
      case CANCEL:
        cancelActivityByCancelCode(joinCode, userNum);
        break;
    }
  }

  /** ================================================= validation ================================================= */

  /**
   * 檢核使用者是否有正在進行的活動
   *
   * @param userNum
   */
  public void validActivityInProgress(Long userNum, boolean needExists) {
    ActivitySetVo activitySetVo = activitySetDao.getInProgressActivity(userNum, ActivityEnum.SetStatus.JOIN.val());
    final boolean isUserJoinActivity = activitySetVo.getAcsNum() != null;
    if (!needExists && isUserJoinActivity) {
      throw new ActivityInProgressException(activitySetVo.getActTitle(), activitySetVo.getHostUserName());
    } else if (needExists && !isUserJoinActivity) {
      throw new ActivityNotFoundException();
    }
  }

  /**
   * 檢核過去有沒有一同參與活動的使用者
   *
   * @param userNum
   */
  public void validHistoryActivitySetUserList(Long userNum) {
    final long userNumCount = activitySetDao.listUserNumByActivityHistory(userNum).stream()
        .filter(num -> !num.equals(userNum))
        .count();
    if (userNumCount == 0) {
      throw new UserNotFoundException();
    }
  }

  /** =================================================== private ================================================== */

  /**
   * 活動第一位參與者
   *
   * @param userNum
   * @param actNum
   * @param startDate
   */
  private void saveFirstActSet(Long userNum, Long actNum, String startDate) {
    ActivitySetVo activitySetVo = new ActivitySetVo();
    activitySetVo.setUserNum(userNum);
    activitySetVo.setActNum(actNum);
    activitySetVo.setStartDate(startDate);
    activitySetVo.setStatus(ActivityEnum.SetStatus.JOIN.val());
    activitySetDao.insert(activitySetVo);

    // 將所有被邀請的群狀態都壓成拒絕
    activitySetDao.query(MapUtil.newHashMap("userNum", userNum)).stream()
        .filter(set -> ActivityEnum.SetStatus.INVITE.val().equals(set.getStatus()))
        .map(set -> {
          set.setStatus(ActivityEnum.SetStatus.CANCEL.val());
          return set;
        })
        .forEach(set -> DaoValidationUtil.validateResultIsOne(() -> activitySetDao.update(set), set));
  }

  /**
   * 使用邀請碼加入活動
   *
   * @param joinCode
   * @param userNum
   * @param event
   */
  private void joinActivitySetByJoinCode(String joinCode, Long userNum, MessageEvent<TextMessageContent> event) {
    final ActivityBasicVo activityBasicVo = activityBasicDao.query(MapUtil.newHashMap("joinCode", joinCode)).stream()
        .findAny()
        .orElseThrow(ActivityNotFoundException::new);
    final Long actNum = activityBasicVo.getActNum();
    final String actTitle = activityBasicVo.getActTitle();
    ActivitySetVo activitySetVo = new ActivitySetVo();
    activitySetVo.setActNum(actNum);
    activitySetVo.setUserNum(userNum);
    List<ActivitySetVo> activitySetVoList = activitySetDao.query(activitySetVo);
    if (activitySetVoList.isEmpty()) {
      activitySetVo.setStatus(ActivityEnum.SetStatus.JOIN.val());
      activitySetVo.setStartDate(yyyy_MM_dd_HH_mm_ss.format(LocalDateTime.now()));
      activitySetDao.insert(activitySetVo);
    } else {
      activitySetVoList.stream()
          .findAny()
          .map(vo -> {
            vo.setStatus(ActivityEnum.SetStatus.JOIN.val());
            return vo;
          })
          .ifPresent(vo -> DaoValidationUtil.validateResultIsOne(() -> activitySetDao.update(vo), vo));
    }

    lineMessageHelper.sendJoinNotify(actTitle, event.getReplyToken(), event.getSource().getUserId());
  }

  /**
   * 使用邀請碼離開活動
   *
   * @param joinCode
   * @param userNum
   * @param event
   */
  private void leaveActivitySetByLeaveCode(String joinCode, Long userNum, MessageEvent<TextMessageContent> event) {
    final ActivityBasicVo activityBasicVo = activityBasicDao.query(MapUtil.newHashMap("joinCode", joinCode)).stream()
        .findAny()
        .orElseThrow(ActivityNotFoundException::new);
    final Long actNum = activityBasicVo.getActNum();
    final String actTitle = activityBasicVo.getActTitle();
    activitySetDao.query(MapUtil.newHashMap("userNum", userNum)).stream()
        .filter(vo -> vo.getEndDate() == null && actNum.equals(vo.getActNum()))
        .findAny()
        .ifPresent(vo -> {
          vo.setStatus(ActivityEnum.SetStatus.LEAVE.val());
          vo.setEndDate(yyyy_MM_dd_HH_mm_ss.format(LocalDateTime.now()));
          DaoValidationUtil.validateResultIsOne(() -> activitySetDao.update(vo), vo);
        });

    lineMessageHelper.sendLeaveNotify(actTitle, event.getReplyToken(), event.getSource().getUserId());
  }

  /**
   * 使用邀請碼加入活動
   *
   * @param joinCode
   * @param userNum
   * @param event
   */
  private void joinActivitySetByInviteCode(String joinCode, Long userNum, MessageEvent<TextMessageContent> event) {
    final ActivityBasicVo activityBasicVo = activityBasicDao.query(MapUtil.newHashMap("joinCode", joinCode)).stream()
        .findAny()
        .orElseThrow(ActivityNotFoundException::new);
    final Long actNum = activityBasicVo.getActNum();
    final String actTitle = activityBasicVo.getActTitle();
    activitySetDao.query(MapUtil.newHashMap("actNum", actNum, "userNum", userNum, "status", ActivityEnum.SetStatus.INVITE.val()))
        .stream()
        .findAny()
        .map(vo -> {
          vo.setStartDate(yyyy_MM_dd_HH_mm_ss.format(LocalDateTime.now()));
          vo.setStatus(ActivityEnum.SetStatus.JOIN.val());
          return vo;
        })
        .ifPresent(vo -> DaoValidationUtil.validateResultIsOne(() -> activitySetDao.update(vo), vo));

    lineMessageHelper.sendJoinNotify(actTitle, event.getReplyToken(), event.getSource().getUserId());
  }

  /**
   * 使用者取消邀請or取消活動
   *
   * @param joinCode
   * @param userNum
   */
  private void cancelActivityByCancelCode(String joinCode, Long userNum) {
    final CancelActivityRvo cancelActivityRvo = activityBasicDao.getAndCancelAllActivitySet(joinCode);
    final boolean isGuest = !userNum.equals(cancelActivityRvo.getHostUserNum());
    final List<ActivitySetVo> activitySetVoList = cancelActivityRvo.getActivitySetVoList();
    final String actTitle = cancelActivityRvo.getActTitle();
    if (isGuest) {
      activitySetVoList.stream()
          .filter(vo -> userNum.equals(vo.getUserNum()))
          .findAny()
          .ifPresent(vo -> {
            DaoValidationUtil.validateResultIsOne(() -> activitySetDao.update(vo), vo);
            lineMessageHelper.sendRejectNotify(actTitle, vo.getUserLineId());
          });
      return;
    }

    final Long actNum = cancelActivityRvo.getActNum();
    final String nowDate = yyyy_MM_dd_HH_mm_ss.format(LocalDateTime.now());
    // 關閉活動
    ActivityBasicVo activityBasicVo = new ActivityBasicVo();
    activityBasicVo.setActNum(actNum);
    activityBasicVo.setEndDate(nowDate);
    activityBasicVo.setActStatus(ActivityEnum.Status.FINISH.val());
    DaoValidationUtil.validateResultIsOne(() -> activityBasicDao.update(activityBasicVo), activityBasicVo);

    // 移除參加者, 發送取消訊息
    activitySetVoList.stream()
        .map(vo -> {
          vo.setEndDate(nowDate);
          vo.setStatus(ActivityEnum.SetStatus.CANCEL.val());
          return vo;
        })
        .forEach(vo -> {
          DaoValidationUtil.validateResultIsOne(() -> activitySetDao.update(vo), vo);
          lineMessageHelper.sendCancelNotify(actTitle, vo.getUserLineId());
        });
  }

}
