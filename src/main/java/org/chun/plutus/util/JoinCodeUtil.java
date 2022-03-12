package org.chun.plutus.util;

import org.chun.plutus.common.enums.JoinCodeEnum;

public class JoinCodeUtil {

  public static String genForceCreateCode(String joinCode){
    return JoinCodeEnum.Action.FORCE_CREATE.val().concat(joinCode);
  }

  public static String genJoinCode(String joinCode) {
    return JoinCodeEnum.Action.JOIN.val().concat(joinCode);
  }

  public static String genInviteJoinCode(String joinCode) {
    return JoinCodeEnum.Action.INVITE.val().concat(joinCode);
  }

  public static String genCancelJoinCode(String joinCode) {
    return JoinCodeEnum.Action.CANCEL.val().concat(joinCode);
  }
}
