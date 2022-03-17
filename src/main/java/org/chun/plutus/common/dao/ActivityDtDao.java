package org.chun.plutus.common.dao;

import org.apache.ibatis.annotations.Param;
import org.chun.plutus.common.dao.base.ActivityDtBaseDao;
import org.chun.plutus.common.vo.ActivityDtVo;

/**
 * For extending functions, edit this file please.
 */
public interface ActivityDtDao extends ActivityDtBaseDao {

  ActivityDtVo getLastActivityByJoinCode(@Param("joinCode") String joinCode);

  ActivityDtVo getLastActivityByUserNum(@Param("userNum") Long userNum);
}
