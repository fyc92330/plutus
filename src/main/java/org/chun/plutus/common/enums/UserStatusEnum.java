package org.chun.plutus.common.enums;

public enum UserStatusEnum {

  /** 使用中 */
  FOLLOW("1"),
  /** 已移除 */
  UNFOLLOW("0");

  private final String status;

  UserStatusEnum(String status) {
    this.status = status;
  }

  public String val() {
    return this.status;
  }
}
