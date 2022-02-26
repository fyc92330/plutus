package org.chun.plutus.common.dao.base;

import java.util.List;
import org.chun.plutus.common.vo.PaymentRecordMTagVo;
import org.apache.ibatis.annotations.Param;

/**
 * Do not modify this file!
 * For extending functions, edit the PaymentRecordMTagDao file please.
 */
public interface PaymentRecordMTagBaseDao {

    List<PaymentRecordMTagVo> query( Object params );

    int insert( Object params );

    List<PaymentRecordMTagVo> listAll();

    Integer count( Object params );

    int delete( Object params );

    PaymentRecordMTagVo getByPk( @Param( "prmtNum" ) Long prmtNum );

    int deleteByPk( @Param( "prmtNum" ) Long prmtNum );

    int update( Object params );

    int forceUpdate( Object params );

}
