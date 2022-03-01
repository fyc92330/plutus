package org.chun.plutus.common.dao.base;

import java.util.List;
import org.chun.plutus.common.vo.AppUserMemberStatusVo;
import org.apache.ibatis.annotations.Param;

/**
 * Do not modify this file!
 * For extending functions, edit the AppUserMemberStatusDao file please.
 */
public interface AppUserMemberStatusBaseDao {

    List<AppUserMemberStatusVo> query( Object params );

    int insert( Object params );

    List<AppUserMemberStatusVo> listAll();

    Integer count( Object params );

    int delete( Object params );

    AppUserMemberStatusVo getByPk( @Param( "aumsNum" ) Long aumsNum );

    int deleteByPk( @Param( "aumsNum" ) Long aumsNum );

    int update( Object params );

    int forceUpdate( Object params );

}
