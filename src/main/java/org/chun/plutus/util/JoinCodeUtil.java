package org.chun.plutus.util;

import org.chun.plutus.common.enums.JoinCodePrefixEnum;

public class JoinCodeUtil {

  public static String genJoinCode(String joinCode) {
    return JoinCodePrefixEnum.JOIN.val().concat(joinCode);
  }

  public static String genInviteJoinCode(String joinCode) {
    return JoinCodePrefixEnum.INVITE.val().concat(joinCode);
  }

  public static String genCancelJoinCode(String joinCode) {
    return JoinCodePrefixEnum.CANCEL.val().concat(joinCode);
  }
}
