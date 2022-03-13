package org.chun.plutus.common.dao;

import org.apache.ibatis.annotations.Param;
import org.chun.plutus.common.dao.base.ActivityBasicBaseDao;
import org.chun.plutus.common.rvo.ActivityViewRvo;
import org.chun.plutus.common.rvo.CancelActivityRvo;
import org.chun.plutus.common.vo.ActivityBasicVo;

/**
 * For extending functions, edit this file please.
 */
public interface ActivityBasicDao extends ActivityBasicBaseDao {

  ActivityBasicVo getByJoinCode(@Param("joinCode") String joinCode);

  ActivityViewRvo getCurrentActivityView(@Param("joinCode") String joinCode, @Param("userNum") Long userNum);

  ActivityBasicVo getActivityBySetUserNum(@Param("userNum") Long userNum);

  ActivityBasicVo getOwnerActivityInfo(@Param("userNum") Long userNum);

  CancelActivityRvo getAndCancelAllActivitySet(@Param("joinCode") String joinCode);
}
