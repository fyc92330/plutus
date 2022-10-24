package org.chun.plutus.common.dao.base;

import java.util.List;

import org.chun.plutus.common.vo.ActivityPaymentVo;
import org.apache.ibatis.annotations.Param;

/**
 * Do not modify this file!
 * For extending functions, edit the ActivityPaymentDao file please.
 */
public interface ActivityPaymentBaseDao {

  List<ActivityPaymentVo> query(Object params);

  int insert(Object params);

  List<ActivityPaymentVo> listAll();

  Integer count(Object params);

  int delete(Object params);

  ActivityPaymentVo getByPk(@Param("acpNum") Long acpNum);

  int deleteByPk(@Param("acpNum") Long acpNum);

  int update(Object params);

  int forceUpdate(Object params);

}
