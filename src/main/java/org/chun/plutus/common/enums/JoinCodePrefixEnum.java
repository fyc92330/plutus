package org.chun.plutus.common.enums;

import java.util.Arrays;

/**
 * JOIN:直接增加
 * LEAVE:將參加中(唯一)的狀態壓離開
 * INVITE:將邀請中的狀態壓參加中
 * CANCEL:將邀請中的狀態壓取消
 */
public enum JoinCodePrefixEnum {

  /** 加入 */
  JOIN("$join-"),
  /** 離開 */
  LEAVE("$leave-"),
  /** 邀請 */
  INVITE("$invite-"),
  /** 取消 */
  CANCEL("$cancel-");

  private final String prefix;

  JoinCodePrefixEnum(String prefix) {
    this.prefix = prefix;
  }

  public String val() {
    return this.prefix;
  }

  public static JoinCodePrefixEnum getEnum(String prefix) {
    return Arrays.stream(values())
        .filter(e -> e.prefix.equals(prefix))
        .findAny()
        .orElseThrow(() -> new EnumConstantNotPresentException(JoinCodePrefixEnum.class, prefix));
  }
}
