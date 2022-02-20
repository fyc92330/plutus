package org.chun.plutus.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chun.plutus.common.constant.CommonConst;
import org.chun.plutus.common.rvo.ApiResponseRvo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ApiResponseRvo handleException(Exception e, HandlerMethod handlerMethod) {
    ApiResponseRvo apiResponseRvo = new ApiResponseRvo();
    try {
      Method method = handlerMethod.getMethod();
      RequestMapping requestMappingAnnotation = method.getAnnotation(RequestMapping.class);
      List<RequestMethod> requestMappingList = Arrays.stream(requestMappingAnnotation.method())
          .filter(requestMethod -> !requestMethod.equals(RequestMethod.OPTIONS))
          .collect(Collectors.toList());
      String errorMsg;
      ;
      if (requestMappingList.size() == 1) {
        switch (requestMappingList.get(0)) {
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
          default:
            errorMsg = CommonConst.COMMON_OTHER_ERROR;
            break;
        }
        // 處理GET POST
      } else if (requestMappingList.containsAll(Arrays.asList(GET, POST))) {
        errorMsg = CommonConst.COMMON_QUERY_ERROR;
      } else {
        errorMsg = CommonConst.COMMON_OTHER_ERROR;
      }

      log.error("", e);
      apiResponseRvo.getErrors().add(errorMsg);

    } catch (Exception ex) {
      log.error("ExceptionHandler Error: {}", ex.getMessage());
    } finally {
      apiResponseRvo.setHttpStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
      apiResponseRvo.setHttpStatusCode(HttpStatus.BAD_REQUEST.value());
    }

    return apiResponseRvo;
  }


}
