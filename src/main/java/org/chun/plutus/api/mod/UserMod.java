package org.chun.plutus.api.mod;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chun.plutus.common.dao.AppUserDao;
import org.chun.plutus.common.exceptions.UserNotFoundException;
import org.chun.plutus.common.vo.AppUserVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserMod {

  private final AppUserDao appUserDao;



  /** =================================================== private ================================================== */

  /**
   * 檢核使用者資訊有無遺失
   *
   * @param userNumList
   */
  public void validUserExists(List<Long> userNumList){
    for(Long userNum : userNumList){
      final boolean isAnyUserNotFound = Optional.ofNullable(appUserDao.getByPk(userNum))
          .map(AppUserVo::getUserLineId)
          .isPresent();
      if(isAnyUserNotFound) throw new UserNotFoundException(userNum);
    }
  }
}
