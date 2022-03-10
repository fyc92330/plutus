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
  /** 建立節點 */
  NODE("$node-"),
  /** 設定分帳方式 */
  SUB_TYPE("$node_type-"),//v1: 均分, 時間分 todo v2:個人分
  /** 設定金額 */
  SUB_COST("$node_cost-"),//todo v2移除
  /** 設定先付款的人 */
  SUB_PAYER("$node_payer-"),//todo v2移除
  /** 檢視 */
  VIEW("$view-"),
  /** 直接建立 */
  FORCE_CREATE("$force_create-");//todo v2移除

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
