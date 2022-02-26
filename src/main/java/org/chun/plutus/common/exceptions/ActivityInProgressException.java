package org.chun.plutus.common.exceptions;

public class ActivityInProgressException extends RuntimeException {

  public ActivityInProgressException(String actTitle, String userName) {
    super(String.format("參與活動進行中, 離開後才能建立新的活動. 活動名稱:%s, 活動發起人:%s", actTitle, userName));
  }
}
