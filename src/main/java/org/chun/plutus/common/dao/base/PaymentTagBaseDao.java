package org.chun.plutus.common.dao.base;

import org.apache.ibatis.annotations.Param;
import org.chun.plutus.common.vo.PaymentTagVo;
import org.chun.plutus.common.vo.PaymentUserVo;

import java.util.List;

public interface PaymentTagBaseDao {

  List<PaymentTagVo> query(Object params);

  int insert(Object params);

  List<PaymentTagVo> listAll();

  Integer count(Object params);

  int delete(Object params);

  PaymentTagVo getByPk(@Param("tagNum") Long tagNum);

  int deleteByPk(@Param("tagNum") Long tagNum);

  int update(Object params);

  int forceUpdate(Object params);
}
