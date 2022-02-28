package org.chun.plutus.common.exceptions;

public class ActivityNotFoundException extends RuntimeException {

  public ActivityNotFoundException() {
    super("查詢不到活動資訊.");
  }
}
