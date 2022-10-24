package org.chun.plutus.common.dao.base;

import java.util.List;

import org.chun.plutus.common.vo.AppUserVo;
import org.apache.ibatis.annotations.Param;

/**
 * Do not modify this file!
 * For extending functions, edit the AppUserDao file please.
 */
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
