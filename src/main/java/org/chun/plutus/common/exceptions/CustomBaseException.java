package org.chun.plutus.common.exceptions;

public class CustomBaseException extends RuntimeException {

  private final String errorMessage;

  CustomBaseException(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public void execException() {
    if (errorMessage != null) throw this;
  }

  public void execExceptionIf(boolean condition) {
    if (condition && errorMessage != null) throw this;
  }
}
