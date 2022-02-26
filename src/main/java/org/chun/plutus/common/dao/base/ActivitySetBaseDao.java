package org.chun.plutus.common.dao.base;

import java.util.List;
import org.chun.plutus.common.vo.ActivitySetVo;
import org.apache.ibatis.annotations.Param;

/**
 * Do not modify this file!
 * For extending functions, edit the ActivitySetDao file please.
 */
public interface ActivitySetBaseDao {

    List<ActivitySetVo> query( Object params );

    int insert( Object params );

    List<ActivitySetVo> listAll();

    Integer count( Object params );

    int delete( Object params );

    ActivitySetVo getByPk( @Param( "acsNum" ) Long acsNum );

    int deleteByPk( @Param( "acsNum" ) Long acsNum );

    int update( Object params );

    int forceUpdate( Object params );

}
