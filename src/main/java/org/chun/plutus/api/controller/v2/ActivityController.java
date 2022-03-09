package org.chun.plutus.api.controller.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chun.plutus.api.facade.ActivityFacade;
import org.chun.plutus.common.rvo.ActivityViewRvo;
import org.chun.plutus.common.vo.ActivityBasicVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/activity")
public class ActivityController {

  private final ActivityFacade activityFacade;

  /**
   * 建立活動
   *
   * @param activityBasicVo
   * @return
   */
  @PostMapping("/create")
  public Long createActivity(@RequestBody ActivityBasicVo activityBasicVo) {
    return activityFacade.createActivity(activityBasicVo);
  }

  /**
   * 活動狀態查看
   *
   * @return
   */
  @GetMapping("/view")
  public ActivityViewRvo viewCurrentActivity(){
    return activityFacade.getActivityView();
  }

  /**
   * 初始化邀請頁面
   *
   * @return
   */
  @GetMapping("/invite")
  public Map<String,Object> initInviteUserList(){
    return activityFacade.getHistoryUserPartnerList();
  }

  /**
   * 邀請申請,發送邀請碼
   *
   * @param userNumList
   */
  @PostMapping("/invite")
  public void inviteUser(@RequestBody List<Long> userNumList){
    activityFacade.sendInviteCode(userNumList);
  }

}
