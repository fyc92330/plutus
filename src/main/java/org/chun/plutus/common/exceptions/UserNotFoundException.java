package org.chun.plutus.common.exceptions;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException() {
    super("查詢不到任何使用者.");
  }

  public UserNotFoundException(Long userNum) {
    super(String.format("查詢不到編號為(%d)的使用者", userNum));
  }
}
