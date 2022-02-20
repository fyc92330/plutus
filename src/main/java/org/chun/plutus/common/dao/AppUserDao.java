package org.chun.plutus.common.dao;

import org.apache.ibatis.annotations.Param;
import org.chun.plutus.common.dao.base.AppUserBaseDao;
import org.chun.plutus.common.vo.AppUserVo;

public interface AppUserDao extends AppUserBaseDao {

  AppUserVo getByUserId(@Param("userId") String userId);
}
