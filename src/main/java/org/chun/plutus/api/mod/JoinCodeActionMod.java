package org.chun.plutus.api.mod;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chun.plutus.common.dao.ActivityBasicDao;
import org.chun.plutus.common.dao.ActivitySetDao;
import org.chun.plutus.common.enums.ActivityEnum;
import org.chun.plutus.common.exceptions.ActivityNotFoundException;
import org.chun.plutus.common.rvo.CancelActivityRvo;
import org.chun.plutus.common.vo.ActivityBasicVo;
import org.chun.plutus.common.vo.ActivitySetVo;
import org.chun.plutus.util.DaoValidationUtil;
import org.chun.plutus.util.MapUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static org.chun.plutus.util.MomentUtil.DateTime.yyyy_MM_dd_HH_mm_ss;

@Slf4j
@RequiredArgsConstructor
@Service
public class JoinCodeActionMod {

  private final ActivityBasicDao activityBasicDao;
  private final ActivitySetDao activitySetDao;
  private final MessageMod messageMod;


  public void saveActivitySetByJoinCode() {

  }


  //====

  /**
   * 使用邀請碼加入活動
   *
   * @param joinCode
   * @param userNum
   * @param event
   */
  public void joinActivitySetByJoinCode(String joinCode, Long userNum, MessageEvent<TextMessageContent> event) {
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

    messageMod.sendJoinNotify(actTitle, event.getReplyToken(), event.getSource().getUserId());
  }

  /**
   * 使用邀請碼離開活動
   *
   * @param joinCode
   * @param userNum
   * @param event
   */
  public void leaveActivitySetByLeaveCode(String joinCode, Long userNum, MessageEvent<TextMessageContent> event) {
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

    messageMod.sendLeaveNotify(actTitle, event.getReplyToken(), event.getSource().getUserId());
  }

  /**
   * 使用邀請碼加入活動
   *
   * @param joinCode
   * @param userNum
   * @param event
   */
  public void joinActivitySetByInviteCode(String joinCode, Long userNum, MessageEvent<TextMessageContent> event) {
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

    messageMod.sendJoinNotify(actTitle, event.getReplyToken(), event.getSource().getUserId());
  }

  /**
   * 使用者取消邀請or取消活動
   *
   * @param joinCode
   * @param userNum
   */
  public void cancelActivityByCancelCode(String joinCode, Long userNum) {
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
            messageMod.sendRejectNotify(actTitle, vo.getUserLineId());
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
          messageMod.sendCancelNotify(actTitle, vo.getUserLineId());
        });
  }
}
