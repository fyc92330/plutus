package org.chun.plutus.common.exceptions;


public class LineClientUserNotFoundException extends RuntimeException {

  public LineClientUserNotFoundException(String clientId) {
    super(String.format("LINE user is not found, client id: %s.", clientId));
  }

  public LineClientUserNotFoundException(){
    super("LINE client is not found.");
  }

}
