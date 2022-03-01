package org.chun.plutus.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chun.plutus.api.mod.ActivityMod;
import org.chun.plutus.api.helper.LineMessageHelper;
import org.chun.plutus.api.mod.UserMod;
import org.chun.plutus.common.mo.InviteJoinCodeMo;
import org.chun.plutus.common.rvo.ActivityViewRvo;
import org.chun.plutus.common.vo.ActivityBasicVo;
import org.chun.plutus.util.MapUtil;
import org.chun.plutus.util.RequestScopeUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class ActivityService {

  private final ActivityMod activityMod;
  private final UserMod userMod;
  private final LineMessageHelper lineMessageHelper;

  /**
   * 建立新活動流程
   * todo 活動建立後建立排程,24小時內沒有人員參與則自動關閉
   *
   * @param activityBasicVo
   */
  public Long createActivity(ActivityBasicVo activityBasicVo) {
    activityMod.validActivityInProgress(RequestScopeUtil.getUserNum(), false);
    return activityMod.saveActivity(activityBasicVo);
  }

  /**
   * 取得當前活動資訊
   *
   * @return
   */
  public ActivityViewRvo getActivityView() {
    final Long userNum = RequestScopeUtil.getUserNum();
    activityMod.validActivityInProgress(userNum, true);
    return activityMod.getCurrentActivity(userNum);
  }

  /**
   * 取得過去曾經一起參與的使用者名單
   *
   * @return
   */
  public Map<String, Object> getHistoryUserPartnerList() {
    final Long userNum = RequestScopeUtil.getUserNum();
    activityMod.validHistoryActivitySetUserList(userNum);
    return MapUtil.newHashMap("userList", activityMod.getUserListToInviteList(userNum));
  }

  public void sendInviteCode(List<Long> userNumList){
    // 檢核userNum有沒有資料 lineId
    userMod.validUserExists(userNumList);
    // 建立邀請群, 取得活動邀請碼
    final InviteJoinCodeMo inviteJoinCodeMo = activityMod.getJoinCodeByCurrentActivity(userNumList);
    // 發送邀請訊息
    lineMessageHelper.pushInviteTemplateMessage(inviteJoinCodeMo);
  }

}
