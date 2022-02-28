package org.chun.plutus.common.enums;

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
  }

  public enum SetStatus {

    /** 參加中 */
    INVITE("0"),
    /** 參加中 */
    JOIN("1"),
    /** 參加中 */
    CANCEL("2"),
    /** 已離開 */
    LEAVE("9");

    private final String status;

    SetStatus(String status) {
      this.status = status;
    }

    public String val() {
      return this.status;
    }
  }
}
