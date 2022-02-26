package org.chun.plutus.api.mod;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.chun.plutus.common.dao.AppUserDao;
import org.chun.plutus.common.vo.ActivityBasicVo;
import org.chun.plutus.common.vo.AppUserVo;
import org.chun.plutus.util.MomentUtil;
import org.chun.plutus.util.RequestScopeUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class ActivityMod {

  private final AppUserDao appUserDao;

  public void createActivity(ActivityBasicVo activityBasicVo) {
    AppUserVo appUserVo = appUserDao.getByPk(RequestScopeUtil.getUserNum());

    activityBasicVo.setUserNum(RequestScopeUtil.getUserNum());
    activityBasicVo.setCreateDate(MomentUtil.DateTime.yyyy_MM_dd_HH_mm_ss.format(LocalDateTime.now()));
    // 組裝邀請碼
    activityBasicVo.setJoinCode(RandomStringUtils.random(8, true, true).toLowerCase());


  }

  /** =================================================== private ================================================== */


}
