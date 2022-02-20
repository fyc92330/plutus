package org.chun.plutus.aop;

import org.chun.plutus.common.rvo.ApiResponseRvo;
import org.chun.plutus.util.JsonBean;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Aspect
@Component
public class PlutusControllerAspect {

  /**
   * 所有Controller, 不包括子資料夾
   */
  @Pointcut("execution(* org.chun.plutus.api.controller.*.*(..))")
  public void controllerPointcut() {
  }

  @Around("controllerPointcut()")
  @SneakyThrows
  public ResponseEntity<ApiResponseRvo> genPlutusReturnObject(ProceedingJoinPoint joinPoint) {
    log.info("-- 進入Controller --");
    /** 回傳物件直接回傳 */
    Object result = joinPoint.proceed(joinPoint.getArgs());
    if (result instanceof ApiResponseRvo) {
      return new ResponseEntity<>(JsonBean.Extra.objectMapper().convertValue(result, ApiResponseRvo.class), HttpStatus.OK);
    }


    Map<String, Object> resultMap = new HashMap<>();
    if (result == null) {
      return new ResponseEntity<>(new ApiResponseRvo(), HttpStatus.OK);
    } else if (result instanceof Collection) {

    } else {
      resultMap = JsonBean.objectMapper().readValue(
          JsonBean.Extra.objectMapper().writeValueAsString(result),
          new TypeReference<Map<String, Object>>() {
          }
      );
    }

    log.info("-- 離開Controller --");
    return new ResponseEntity<>(new ApiResponseRvo(resultMap), HttpStatus.OK);
  }
}
