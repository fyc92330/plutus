package org.chun.plutus.common.dao;

import org.apache.ibatis.annotations.Param;
import org.chun.plutus.common.dao.base.PaymentTagGroupBaseDao;
import org.chun.plutus.common.vo.PaymentTagGroupVo;

import java.util.List;

public interface PaymentTagGroupDao extends PaymentTagGroupBaseDao {

  List<PaymentTagGroupVo> listGroupListWithTagsByUser(@Param("userNum") Long userNum);
}
