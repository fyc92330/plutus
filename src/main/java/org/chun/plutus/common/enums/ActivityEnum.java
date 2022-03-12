package org.chun.plutus.common.enums;

import java.util.Arrays;

public class ActivityEnum {

  public enum Status {

    /** 準備中 */
    PREPARE("0"),
    /** 進行中 */
    PROGRESS("1"),
    /** 結束 */
    FINISH("9");

    private final String status;

    Status(String status) {
      this.status = status;
    }

    public String val() {
      return this.status;
    }

    public static Status getEnum(String status) {
      return Arrays.stream(values())
          .filter(e -> e.val().equals(status))
          .findAny()
          .orElseThrow(() -> new EnumConstantNotPresentException(Status.class, status));
    }
  }

  public enum SetStatus {

    /** 邀請中 */
    INVITE("0"),
    /** 參加中 */
    JOIN("1"),
    /** 已拒絕 */
    REJECT("2"),
    /** 已取消 */
    CANCEL("8"),
    /** 已離開 */
    LEAVE("9");

    private final String status;

    SetStatus(String status) {
      this.status = status;
    }

    public String val() {
      return this.status;
    }

    public static SetStatus getEnum(String status) {
      return Arrays.stream(values())
          .filter(e -> e.val().equals(status))
          .findAny()
          .orElseThrow(() -> new EnumConstantNotPresentException(Status.class, status));
    }
  }

  public enum PayType {

    /** 預設:平均分配 */
    DEFAULT("0","平均分配"),
    /** 平均分配 */
    AVERAGE("1","平均分配"),
    /** 依時間比例分 */
    SCALE("2","依時間比例分"),
    /** 選擇性分配 */
    CHOICE("3","依時間比例分");

    private final String type;

    private final String name;

    PayType(String type, String name) {
      this.type = type;
      this.name = name;
    }

    public String val() {
      return this.type;
    }

    public String getSimpleName() {
      return
    }

    public static PayType getEnum(String type) {
      return Arrays.stream(values())
          .filter(e -> e.val().equals(type))
          .findAny()
          .orElseThrow(() -> new EnumConstantNotPresentException(ActivityPayTypeEnum.class, type));
    }
  }
}
