package org.chun.plutus.common.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum RecordTypeEnum {

  /** 收入 */
  INCOME("1", "\u6536\u5165"),
  /** 支出 */
  PAYOUT("2", "\u652f\u51fa");

  private final String recordType;

  private final String recordTypeName;

  RecordTypeEnum(String recordType, String recordTypeName) {
    this.recordType = recordType;
    this.recordTypeName = recordTypeName;
  }

  public static RecordTypeEnum getEnum(String type) {
    return Arrays.stream(values())
        .filter(e -> e.recordType.equals(type))
        .findAny()
        .orElseThrow(() -> new EnumConstantNotPresentException(RecordTypeEnum.class, type));
  }
}
