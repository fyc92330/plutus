package org.chun.plutus.common.dao.base;

import org.apache.ibatis.annotations.Param;
import org.chun.plutus.common.vo.PaymentRecordMTagVo;
import org.chun.plutus.common.vo.PaymentUserVo;

import java.util.List;

public interface PaymentRecordMTagBaseDao {

  List<PaymentRecordMTagVo> query(Object params);

  int insert(Object params);

  List<PaymentRecordMTagVo> listAll();

  Integer count(Object params);

  int delete(Object params);

  PaymentRecordMTagVo getByPk(@Param("prmtNum") Long prmtNum);

  int deleteByPk(@Param("prmtNum") Long prmtNum);

  int update(Object params);

  int forceUpdate(Object params);
}
