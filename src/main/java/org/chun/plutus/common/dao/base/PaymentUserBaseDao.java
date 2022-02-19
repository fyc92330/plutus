package org.chun.plutus.common.dao.base;

import org.apache.ibatis.annotations.Param;
import org.chun.plutus.common.vo.PaymentUserVo;

import java.util.List;

public interface PaymentUserBaseDao {

  List<PaymentUserVo> query(Object params);

  int insert(Object params);

  List<PaymentUserVo> listAll();

  Integer count(Object params);

  int delete(Object params);

  PaymentUserVo getByPk(@Param("userNum") Long userNum);

  int deleteByPk(@Param("userNum") Long userNum);

  int update(Object params);

  int forceUpdate(Object params);
}
