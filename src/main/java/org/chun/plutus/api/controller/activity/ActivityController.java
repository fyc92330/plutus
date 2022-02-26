package org.chun.plutus.api.controller.activity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chun.plutus.api.mod.ActivityMod;
import org.chun.plutus.common.dao.AppUserDao;
import org.chun.plutus.common.vo.ActivityBasicVo;
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

  @PostMapping("/create")
  public void createActivity(@RequestBody ActivityBasicVo activityBasicVo) {
    activityMod.createActivity(activityBasicVo);
  }


}
