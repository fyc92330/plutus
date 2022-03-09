package org.chun.plutus.common.enums;

import java.util.Arrays;

public enum ActivityPayTypeEnum {

  /** 平均分配 */
  AVERAGE("1"),
  /** 依時間比例分 */
  SCALE("2"),
  /** 選擇性分配 */
  CHOICE("3");

  private final String type;

  ActivityPayTypeEnum(String type) {
    this.type = type;
  }

  public String val() {
    return this.type;
  }

  public static ActivityPayTypeEnum getEnum(String type) {
    return Arrays.stream(values())
        .filter(e -> e.val().equals(type))
        .findAny()
        .orElseThrow(() -> new EnumConstantNotPresentException(ActivityPayTypeEnum.class, type));
  }
}
