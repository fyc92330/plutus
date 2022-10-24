package org.chun.plutus.common.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CustomBaseException extends RuntimeException {

  CustomBaseException(String errorMsg) {
    super(errorMsg);
  }

}
