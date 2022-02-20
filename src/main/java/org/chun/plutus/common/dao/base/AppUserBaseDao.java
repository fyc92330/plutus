package org.chun.plutus.common.dao.base;

import org.apache.ibatis.annotations.Param;
import org.chun.plutus.common.vo.AppUserVo;

import java.util.List;

public interface AppUserBaseDao {

  List<AppUserVo> query(Object params);

  int insert(Object params);

  List<AppUserVo> listAll();

  Integer count(Object params);

  int delete(Object params);

  AppUserVo getByPk(@Param("userNum") Long userNum);

  int deleteByPk(@Param("userNum") Long userNum);

  int update(Object params);

  int forceUpdate(Object params);
}
