package org.chun.plutus.aop;

import org.chun.plutus.common.ro.PlutusApiRo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public PlutusApiRo handleException(Exception e) {
    log.error("Exception ---> ", e);
    return new PlutusApiRo(e.getMessage());
  }
}
