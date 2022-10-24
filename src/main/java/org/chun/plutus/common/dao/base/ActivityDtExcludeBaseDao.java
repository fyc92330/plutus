package org.chun.plutus.common.dao.base;

import java.util.List;

import org.chun.plutus.common.vo.ActivityDtExcludeVo;
import org.apache.ibatis.annotations.Param;

/**
 * Do not modify this file!
 * For extending functions, edit the ActivityDtExcludeDao file please.
 */
public interface ActivityDtExcludeBaseDao {

  List<ActivityDtExcludeVo> query(Object params);

  int insert(Object params);

  List<ActivityDtExcludeVo> listAll();

  Integer count(Object params);

  int delete(Object params);

  ActivityDtExcludeVo getByPk(@Param("aceNum") Long aceNum);

  int deleteByPk(@Param("aceNum") Long aceNum);

  int update(Object params);

  int forceUpdate(Object params);

}
