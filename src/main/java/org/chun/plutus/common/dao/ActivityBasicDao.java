package org.chun.plutus.common.dao;

import org.apache.ibatis.annotations.Param;
import org.chun.plutus.common.dao.base.ActivityBasicBaseDao;
import org.chun.plutus.common.rvo.ActivityViewRvo;

/**
 * For extending functions, edit this file please.
 */
public interface ActivityBasicDao extends ActivityBasicBaseDao {

  ActivityViewRvo getCurrentActivityView(@Param("userNum") Long userNum);
}
