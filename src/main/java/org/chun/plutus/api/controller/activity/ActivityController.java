package org.chun.plutus.api.controller.activity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chun.plutus.api.mod.ActivityMod;
import org.chun.plutus.common.dao.AppUserDao;
import org.chun.plutus.common.vo.ActivityVo;
import org.chun.plutus.common.vo.AppUserVo;
import org.chun.plutus.util.RequestScopeUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/act")
public class ActivityController {

  private final ActivityMod activityMod;
  private final AppUserDao appUserDao;

  @PostMapping("/create")
  public void createActivity(@RequestBody ActivityVo activityVo) {
    AppUserVo appUserVo = appUserDao.getByPk(RequestScopeUtil.getUserNum());
    activityMod.createActivity(appUserVo);
  }


}
