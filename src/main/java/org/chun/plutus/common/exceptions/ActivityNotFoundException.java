package org.chun.plutus.common.exceptions;

public class ActivityNotFoundException extends CustomBaseException {

  public ActivityNotFoundException() {
    super("沒有參加中的活動");
  }
}
