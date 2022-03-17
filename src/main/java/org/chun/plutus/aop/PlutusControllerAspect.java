package org.chun.plutus.aop;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.chun.plutus.common.rvo.ApiResponseRvo;
import org.chun.plutus.util.JsonBean;
import org.chun.plutus.util.MapUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
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
      resultMap = MapUtil.newHashMap("result", result);
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

  @Before(value = "controllerPointcut()")
  protected void beforeAdvice(JoinPoint joinPoint) {

    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    final String authorizationToken = request.getHeader(HttpHeaders.AUTHORIZATION);
    // 解開jwt取得userNum
    final Long userNum = 0L;

    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
    assert requestAttributes != null;
//    requestAttributes.setAttribute(RequestScopeConst.USER_NUM_STR, userNum, SCOPE_REQUEST);
  }

}
