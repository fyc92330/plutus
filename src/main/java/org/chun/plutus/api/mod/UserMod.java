package org.chun.plutus.api.mod;

import com.linecorp.bot.model.profile.UserProfileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chun.lineBot.ILineBotService;
import org.chun.plutus.common.dao.AppUserDao;
import org.chun.plutus.common.dao.AppUserMemberStatusDao;
import org.chun.plutus.common.enums.UserStatusEnum;
import org.chun.plutus.common.exceptions.UserNotFoundException;
import org.chun.plutus.common.vo.AppUserMemberStatusVo;
import org.chun.plutus.common.vo.AppUserVo;
import org.chun.plutus.util.DaoValidationUtil;
import org.chun.plutus.util.MapUtil;
import org.chun.plutus.util.MomentUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserMod {

  private final ILineBotService lineBotService;
  private final AppUserDao appUserDao;
  private final AppUserMemberStatusDao appUserMemberStatusDao;

  /**
   * 取得or建立使用者
   *
   * @param userId
   * @return
   */
  public AppUserVo saveAppUser(String userId) {
    return Optional.ofNullable(appUserDao.getByUserId(userId))
        .orElseGet(() -> {
          UserProfileResponse userProfileResponse = lineBotService.profile(userId);
          AppUserVo appUserVo = new AppUserVo();
          appUserVo.setUserLineName(userProfileResponse.getDisplayName());
          appUserVo.setUserLineId(userId);
          appUserVo.setUserLinePic(userProfileResponse.getPictureUrl().toString());
          appUserDao.insert(appUserVo);
          return appUserVo;
        });
  }

  /**
   * 建立or編輯使用者狀態紀錄
   *
   * @param userNum
   */
  public void handleFollowEvent(Long userNum) {
    final String nowDate = MomentUtil.DateTime.yyyy_MM_dd_HH_mm_ss.format(LocalDateTime.now());
    AppUserMemberStatusVo appUserMemberStatusVo = appUserMemberStatusDao.query(MapUtil.newHashMap("userNum", userNum))
        .stream()
        .findAny()
        .map(vo -> {
          vo.setUnFollowTime(vo.getUnFollowTime() + 1);
          vo.setFollowDate(nowDate);
          return vo;
        })
        .orElseGet(() -> {
          AppUserMemberStatusVo userStatusVo = new AppUserMemberStatusVo();
          userStatusVo.setUserNum(userNum);
          userStatusVo.setUserStatus(UserStatusEnum.FOLLOW.val());
          userStatusVo.setCreateDate(nowDate);
          userStatusVo.setFollowDate(nowDate);
          return userStatusVo;
        });

    DaoValidationUtil.validateResultIsOne(() -> appUserMemberStatusVo.getAumsNum() == null
            ? appUserMemberStatusDao.insert(appUserMemberStatusVo)
            : appUserMemberStatusDao.update(appUserMemberStatusVo)
        , appUserMemberStatusVo);
  }

  /**
   * 紀錄移除頻道使用者
   *
   * @param userNum
   */
  public void handleUnFollowEvent(Long userNum) {
    final String nowDate = MomentUtil.DateTime.yyyy_MM_dd_HH_mm_ss.format(LocalDateTime.now());
    AppUserMemberStatusVo appUserMemberStatusVo = appUserMemberStatusDao.query(MapUtil.newHashMap("userNum", userNum))
        .stream()
        .findAny()
        .map(vo -> {
          vo.setUnFollowDate(nowDate);
          vo.setUnFollowTime(vo.getUnFollowTime() + 1);
          return vo;
        })
        .orElseGet(() -> {
          AppUserMemberStatusVo userStatusVo = new AppUserMemberStatusVo();
          userStatusVo.setUserNum(userNum);
          userStatusVo.setUserStatus(UserStatusEnum.UNFOLLOW.val());
          userStatusVo.setCreateDate(nowDate);
          userStatusVo.setUnFollowDate(nowDate);
          userStatusVo.setUnFollowTime(1L);
          return userStatusVo;
        });

    DaoValidationUtil.validateResultIsOne(() -> appUserMemberStatusVo.getAumsNum() == null
            ? appUserMemberStatusDao.insert(appUserMemberStatusVo)
            : appUserMemberStatusDao.update(appUserMemberStatusVo)
        , appUserMemberStatusVo);
  }

  /** =================================================== private ================================================== */

  /**
   * 檢核使用者資訊有無遺失
   *
   * @param userNumList
   */
  public void validUserExists(List<Long> userNumList) {
    for (Long userNum : userNumList) {
      final boolean isAnyUserNotFound = Optional.ofNullable(appUserDao.getByPk(userNum))
          .map(AppUserVo::getUserLineId)
          .isPresent();
      if (isAnyUserNotFound) throw new UserNotFoundException(userNum);
    }
  }
}
