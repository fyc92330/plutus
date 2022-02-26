package org.chun.plutus.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chun.plutus.api.mod.ActivityMod;
import org.chun.plutus.common.rvo.ActivityViewRvo;
import org.chun.plutus.common.vo.ActivityBasicVo;
import org.chun.plutus.util.RequestScopeUtil;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ActivityService {

  private final ActivityMod activityMod;

  /**
   * 建立新活動流程
   * todo 活動建立後建立排程,24小時內沒有人員參與則自動關閉
   *
   * @param activityBasicVo
   */
  public Long createActivity(ActivityBasicVo activityBasicVo){
    activityMod.validActivityInProgress(RequestScopeUtil.getUserNum(), false);
    return activityMod.saveActivity(activityBasicVo);
  }

  public ActivityViewRvo getActivityView(){
    final Long userNum = RequestScopeUtil.getUserNum();
    activityMod.validActivityInProgress(userNum, true);
  }

}
