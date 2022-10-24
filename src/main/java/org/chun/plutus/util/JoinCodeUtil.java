package org.chun.plutus.util;

import org.chun.plutus.common.enums.MenuEnum;

public class JoinCodeUtil {

  public static String genForceCreateCode(String joinCode) {
    return MenuEnum.Action.FORCE_CREATE.val().concat(joinCode);
  }

  public static String genJoinCode(String joinCode) {
    return MenuEnum.Action.JOIN.val().concat(joinCode);
  }

  public static String genInviteJoinCode(String joinCode) {
    return MenuEnum.Action.INVITE.val().concat(joinCode);
  }

  public static String genCancelJoinCode(String joinCode) {
    return MenuEnum.Action.CANCEL.val().concat(joinCode);
  }
}
