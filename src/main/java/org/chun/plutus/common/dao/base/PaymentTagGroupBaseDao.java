package org.chun.plutus.common.dao.base;

import org.apache.ibatis.annotations.Param;
import org.chun.plutus.common.vo.PaymentTagGroupVo;
import org.chun.plutus.common.vo.PaymentUserVo;

import java.util.List;

public interface PaymentTagGroupBaseDao {

  List<PaymentTagGroupVo> query(Object params);

  int insert(Object params);

  List<PaymentTagGroupVo> listAll();

  Integer count(Object params);

  int delete(Object params);

  PaymentTagGroupVo getByPk(@Param("tagGroupNum") Long tagGroupNum);

  int deleteByPk(@Param("tagGroupNum") Long tagGroupNum);

  int update(Object params);

  int forceUpdate(Object params);
}
