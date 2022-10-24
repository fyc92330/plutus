package org.chun.plutus.common.dao.base;

import java.util.List;

import org.chun.plutus.common.vo.ActivityDtVo;
import org.apache.ibatis.annotations.Param;

/**
 * Do not modify this file!
 * For extending functions, edit the ActivityDtDao file please.
 */
public interface ActivityDtBaseDao {

  List<ActivityDtVo> query(Object params);

  int insert(Object params);

  List<ActivityDtVo> listAll();

  Integer count(Object params);

  int delete(Object params);

  ActivityDtVo getByPk(@Param("acdNum") Long acdNum);

  int deleteByPk(@Param("acdNum") Long acdNum);

  int update(Object params);

  int forceUpdate(Object params);

}
