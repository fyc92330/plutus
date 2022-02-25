package org.chun.plutus.api.mod;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.chun.plutus.common.vo.ActivityVo;
import org.chun.plutus.common.vo.AppUserVo;
import org.chun.plutus.util.MomentUtil;
import org.chun.plutus.util.RequestScopeUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class ActivityMod {

  public void createActivity(AppUserVo appUserVo) {
    ActivityVo activityVo = new ActivityVo();
    activityVo.setUserNum(RequestScopeUtil.getUserNum());
    activityVo.setCreateUser(appUserVo.getUserName());
    activityVo.setCreateDate(MomentUtil.DateTime.yyyy_MM_dd_HH_mm_ss.format(LocalDateTime.now()));
    // 組裝邀請碼
    activityVo.setJoinCode(RandomStringUtils.random(8, true, true).toLowerCase());


  }

  /** =================================================== private ================================================== */


}
