package org.chun.plutus.common.dao.base;

import org.apache.ibatis.annotations.Param;
import org.chun.plutus.common.vo.PaymentRecordVo;
import org.chun.plutus.common.vo.PaymentUserVo;

import java.util.List;

public interface PaymentRecordBaseDao {

  List<PaymentRecordVo> query(Object params);

  int insert(Object params);

  List<PaymentRecordVo> listAll();

  Integer count(Object params);

  int delete(Object params);

  PaymentRecordVo getByPk(@Param("paymentNum") Long paymentNum);

  int deleteByPk(@Param("paymentNum") Long paymentNum);

  int update(Object params);

  int forceUpdate(Object params);
}
