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

  ActivitySetVo getInProgressActivity(@Param("userNum") Long userNum, @Param("status") String status);

  List<Long> listUserNumByActivityHistory(@Param("userNum") Long userNum);

  List<AppUserVo> listUserListByActivityHistory(@Param("userNum") Long userNum);
}
