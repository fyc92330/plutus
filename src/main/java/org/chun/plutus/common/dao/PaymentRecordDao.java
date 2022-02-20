package org.chun.plutus.common.dao;

import org.chun.plutus.common.dao.base.PaymentRecordBaseDao;
import org.chun.plutus.common.vo.PaymentRecordVo;

import java.util.List;

public interface PaymentRecordDao extends PaymentRecordBaseDao {

  List<PaymentRecordVo> listByQueryCondition(Object param);
}
