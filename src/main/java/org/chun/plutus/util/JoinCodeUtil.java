package org.chun.plutus.util;

import org.chun.plutus.common.enums.JoinCodeEnum;

public class JoinCodeUtil {

  public static String genForceCreateCode(String joinCode){
    return JoinCodeEnum.FORCE_CREATE.val().concat(joinCode);
  }

  public static String genJoinCode(String joinCode) {
    return JoinCodeEnum.JOIN.val().concat(joinCode);
  }

  public static String genInviteJoinCode(String joinCode) {
    return JoinCodeEnum.INVITE.val().concat(joinCode);
  }

  public static String genCancelJoinCode(String joinCode) {
    return JoinCodeEnum.CANCEL.val().concat(joinCode);
  }
}
