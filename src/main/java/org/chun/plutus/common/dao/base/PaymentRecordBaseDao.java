package org.chun.plutus.common.dao.base;

import java.util.List;
import org.chun.plutus.common.vo.PaymentRecordVo;
import org.apache.ibatis.annotations.Param;

/**
 * Do not modify this file!
 * For extending functions, edit the PaymentRecordDao file please.
 */
public interface PaymentRecordBaseDao {

    List<PaymentRecordVo> query( Object params );

    int insert( Object params );

    List<PaymentRecordVo> listAll();

    Integer count( Object params );

    int delete( Object params );

    PaymentRecordVo getByPk( @Param( "paymentNum" ) Long paymentNum );

    int deleteByPk( @Param( "paymentNum" ) Long paymentNum );

    int update( Object params );

    int forceUpdate( Object params );

}
