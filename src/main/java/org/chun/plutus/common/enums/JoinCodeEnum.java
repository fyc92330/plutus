package org.chun.plutus.common.enums;

import java.util.Arrays;

public enum JoinCodeEnum {

  /** 加入 */
  JOIN("$join-"),
  /** 離開 */
  LEAVE("$leave-"),
  /** 邀請 */
  INVITE("$invite-"),
  /** 取消 */
  CANCEL("$cancel-"),
  /** 建立 */
  CREATE("$create-"),
  /** 關閉 */
  CLOSE("$close-"),
  /** 開啟子活動 */
  SUB_CREATE("$sub_start-"),
  /** 結束子活動 */
  SUB_CLOSE("$sub_end-"),
  /** 設定分帳方式 */
  SUB_TYPE("$sub_type-"),//v1: 均分, 時間分 todo v2:個人分
  /** 設定金額 */
  SUB_COST("$sub_cost-"),//todo v2移除
  /** 設定先付款的人 */
  SUB_PAYER("$sub_payer-"),//todo v2移除
  /** 檢視 */
  VIEW("$view-");//todo v2移除

  private final String prefix;

  JoinCodeEnum(String prefix) {
    this.prefix = prefix;
  }

  public String val() {
    return this.prefix;
  }

  public static JoinCodeEnum getEnum(String prefix) {
    return Arrays.stream(values())
        .filter(e -> e.prefix.equals(prefix))
        .findAny()
        .orElseThrow(() -> new EnumConstantNotPresentException(JoinCodeEnum.class, prefix));
  }
}
