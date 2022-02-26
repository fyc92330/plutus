package org.chun.plutus.api.mod;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.chun.plutus.common.dao.ActivityBasicDao;
import org.chun.plutus.common.dao.ActivitySetDao;
import org.chun.plutus.common.enums.ActivityEnum;
import org.chun.plutus.common.exceptions.ActivityInProgressException;
import org.chun.plutus.common.exceptions.ActivityNotFoundException;
import org.chun.plutus.common.rvo.ActivityViewRvo;
import org.chun.plutus.common.vo.ActivityBasicVo;
import org.chun.plutus.common.vo.ActivitySetVo;
import org.chun.plutus.util.RequestScopeUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static org.chun.plutus.util.MomentUtil.DateTime.yyyy_MM_dd_HH_mm_ss;

@Slf4j
@RequiredArgsConstructor
@Service
public class ActivityMod {

  private final ActivityBasicDao activityBasicDao;
  private final ActivitySetDao activitySetDao;

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
  public ActivityViewRvo getCurrentActivity(Long userNum){
    return activityBasicDao.getCurrentActivityView(userNum);
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
    } else if(needExists && !isUserJoinActivity){
      throw new ActivityNotFoundException();
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
  }

}
