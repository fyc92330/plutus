package org.chun.plutus.common.enums;

import java.util.Arrays;

public class MenuEnum {

  public enum Action {
    /** 加入 */
    JOIN("$join-"),
    /** 離開 */
    LEAVE("$leave"),
    /** 邀請 */
    INVITE("$invite-"),
    /** 取消 */
    CANCEL("$cancel-"),
    /** 建立 */
    CREATE("$create"),
    /** 關閉 */
    CLOSE("$close"),
    /** 建立節點 */
    NODE("$node"),
    /** Menu Action */
    MENU("$menu-"),
    /** 檢視 */
    VIEW("$view"),
    /** 返回主選單 */
    MAIN_MENU("$main_selector"),
    /** 開啟子選單 */
    SUB_MENU("$sub_selector"),
    /** 叫出template */
    CALL_MENU("$call_menu"),
    /** QrCode */
    QRCODE("$qrcode"),
    /** 直接建立 */
    FORCE_CREATE("$force_create");//todo v2移除

    private final String prefix;

    Action(String prefix) {
      this.prefix = prefix;
    }

    public String val() {
      return this.prefix;
    }

    public static Action getEnum(String prefix) {
      return Arrays.stream(values())
          .filter(e -> e.prefix.equals(prefix))
          .findAny()
          .orElseThrow(() -> new EnumConstantNotPresentException(Action.class, prefix));
    }
  }

  public enum Setting {

    /** 設定活動子標題 */
    TITLE("title-"),
    /** 設定子活動花費 */
    COST("cost-"),
    /** 設定預先付款人 */
    PAYER("payfor-"),
    /** 設定子活動為按時間拆帳 */
    TYPE_AVERAGE("type1"),
    /** 設定子活動為平均拆帳 */
    TYPE_SCALE("type2"),
    /** 設定子活動為特殊拆帳 */
    TYPE_CHOICE("type3");

    private final String action;

    Setting(String action) {
      this.action = action;
    }

    public String val() {
      return this.action;
    }

    public static Setting getEnum(String action) {
      return Arrays.stream(values())
          .filter(e -> e.action.equals(action))
          .findAny()
          .orElseThrow(() -> new EnumConstantNotPresentException(Setting.class, action));
    }

  }
}
