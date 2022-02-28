package org.chun.plutus.util;

import org.chun.plutus.common.constant.JoinCodeConst;

public class JoinCodeUtil {

  public static String genJoinCode(String joinCode){
    return JoinCodeConst.JOIN_PREFIX.concat(joinCode);
  }

  public static String genInviteJoinCode(String joinCode){
    return JoinCodeConst.INVITE_JOIN_PREFIX.concat(joinCode);
  }

  public static String genCancelJoinCode(String joinCode){
    return JoinCodeConst.CANCEL_PREFIX.concat(joinCode);
  }
}
