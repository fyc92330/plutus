package org.chun.plutus.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.chun.plutus.common.constant.CommonConst;
import org.chun.plutus.common.rvo.ApiResponseRvo;
import org.chun.plutus.util.MapUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ApiResponseRvo handleException(Exception e, HandlerMethod handlerMethod) {
    ApiResponseRvo apiResponseRvo = new ApiResponseRvo();
    try {
      final RequestMethod requestMethod =
          Arrays.stream(handlerMethod.getMethod().getAnnotations())
              .map(Annotation::getClass)
              .map(clazz -> clazz.getAnnotation(RequestMapping.class))
              .filter(Objects::nonNull)
              .findAny()
              .map(RequestMapping::method)
              .map(array -> Arrays.asList(array).get(0))
              .orElse(RequestMethod.OPTIONS);

      String errorMsg = Strings.EMPTY;
      switch (requestMethod) {
        case GET:
          errorMsg = CommonConst.COMMON_QUERY_ERROR;
          break;
        case POST:
          errorMsg = CommonConst.COMMON_INSERT_ERROR;
          break;
        case PUT:
          errorMsg = CommonConst.COMMON_UPDATE_ERROR;
          break;
        case DELETE:
          errorMsg = CommonConst.COMMON_DELETE_ERROR;
          break;
      }

      log.error("", e);
      apiResponseRvo.getErrors().add(StringUtils.isNotBlank(errorMsg) ? errorMsg : CommonConst.COMMON_OTHER_ERROR);

    } catch (Exception ex) {
      log.error("ExceptionHandler Error: {}", ex.getMessage());
    } finally {
      apiResponseRvo.setHttpStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
      apiResponseRvo.setHttpStatusCode(HttpStatus.BAD_REQUEST.value());
    }

    return apiResponseRvo;
  }


}
