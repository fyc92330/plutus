package org.chun.plutus.common.dao;

import org.apache.ibatis.annotations.Param;
import org.chun.plutus.common.dao.base.ActivitySetBaseDao;
import org.chun.plutus.common.vo.ActivitySetVo;
import org.chun.plutus.common.vo.AppUserVo;

import java.util.List;

/**
 * For extending functions, edit this file please.
 */
public interface ActivitySetDao extends ActivitySetBaseDao {

  /** 以狀態取得使用者參加中的活動 */
  ActivitySetVo getInProgressActivity(@Param("userNum") Long userNum, @Param("status") String status);

  /** 以邀請碼取得使用者參加中的活動 */
  ActivitySetVo getByUserNumAndJoinCode(@Param("userNum") Long userNum, @Param("joinCode") String joinCode);

  List<Long> listUserNumByActivityHistory(@Param("userNum") Long userNum);

  List<AppUserVo> listUserListByActivityHistory(@Param("userNum") Long userNum);
}
