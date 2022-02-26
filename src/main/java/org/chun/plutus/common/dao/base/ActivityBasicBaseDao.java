package org.chun.plutus.common.dao.base;

import java.util.List;
import org.chun.plutus.common.vo.ActivityBasicVo;
import org.apache.ibatis.annotations.Param;

/**
 * Do not modify this file!
 * For extending functions, edit the ActivityBasicDao file please.
 */
public interface ActivityBasicBaseDao {

    List<ActivityBasicVo> query( Object params );

    int insert( Object params );

    List<ActivityBasicVo> listAll();

    Integer count( Object params );

    int delete( Object params );

    ActivityBasicVo getByPk( @Param( "actNum" ) Long actNum );

    int deleteByPk( @Param( "actNum" ) Long actNum );

    int update( Object params );

    int forceUpdate( Object params );

}
