package org.chun.plutus.common.exceptions;

public class ActivityInProgressException extends RuntimeException {

  //todo v2 加上參與者
  public ActivityInProgressException(String actTitle, String userName, boolean isHost) {
    super(String.format("參與活動進行中, 離開後才能建立新的活動. 活動名稱:%s, 活動發起人:%s", actTitle, userName));
  }

  public ActivityInProgressException(Long userNum, Long actNum){
    super(String.format("已加入此活動. userNum:%d, actNum:%d", userNum, actNum));
  }
}
