package org.chun.plutus.common.constant;

/**
 * 格式:https://line.me/R/oaMessage/@530vubeg/?$/join-12345678
 *
 */
public class JoinCodeConst {

  public static final String URL = "https://line.me/R/oaMessage/{lineBotId}/?{inputMsg}";

  public static final String JOIN_PREFIX = "$join-";

  public static final String LEAVE_PREFIX = "$leave-";

  public static final String INVITE_JOIN_PREFIX = "$invite-";

  public static final String CANCEL_PREFIX = "$cancel-";
}
