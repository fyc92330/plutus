package org.chun.plutus.common.dao.base;

import java.util.List;
import org.chun.plutus.common.vo.PaymentTagGroupVo;
import org.apache.ibatis.annotations.Param;

/**
 * Do not modify this file!
 * For extending functions, edit the PaymentTagGroupDao file please.
 */
public interface PaymentTagGroupBaseDao {

    List<PaymentTagGroupVo> query( Object params );

    int insert( Object params );

    List<PaymentTagGroupVo> listAll();

    Integer count( Object params );

    int delete( Object params );

    PaymentTagGroupVo getByPk( @Param( "tagGroupNum" ) Long tagGroupNum );

    int deleteByPk( @Param( "tagGroupNum" ) Long tagGroupNum );

    int update( Object params );

    int forceUpdate( Object params );

}
