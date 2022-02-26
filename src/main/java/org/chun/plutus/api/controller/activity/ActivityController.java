package org.chun.plutus.api.controller.activity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chun.plutus.api.mod.ActivityMod;
import org.chun.plutus.api.service.ActivityService;
import org.chun.plutus.common.dao.AppUserDao;
import org.chun.plutus.common.rvo.ActivityViewRvo;
import org.chun.plutus.common.vo.ActivityBasicVo;
import org.chun.plutus.common.vo.AppUserVo;
import org.chun.plutus.util.RequestScopeUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/activity")
public class ActivityController {

  private final ActivityService activityService;

  /**
   * 建立活動
   *
   * @param activityBasicVo
   * @return
   */
  @PostMapping("/create")
  public Long createActivity(@RequestBody ActivityBasicVo activityBasicVo) {
    return activityService.createActivity(activityBasicVo);
  }

  /**
   * 活動狀態查看
   *
   * @return
   */
  @GetMapping("/view")
  public ActivityViewRvo viewCurrentActivity(){
    return activityService.getActivityView();
  }

}
