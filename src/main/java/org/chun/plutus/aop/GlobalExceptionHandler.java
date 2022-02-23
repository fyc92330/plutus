package org.chun.plutus.aop;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chun.annotation.QueryMapping;
import org.chun.plutus.common.constant.CommonConst;
import org.chun.plutus.common.rvo.ApiResponseRvo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Objects;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.OPTIONS;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ApiResponseRvo handleException(Exception e, HandlerMethod handlerMethod) {
    ApiResponseRvo apiResponseRvo = new ApiResponseRvo();
    try {
      final RequestMethod requestMethod = Arrays.stream(handlerMethod.getMethod().getAnnotations())
          .map(Annotation::annotationType)
          .map(RequestMethodEnum::getEnum)
          .filter(Objects::nonNull)
          .findAny()
          .map(RequestMethodEnum::getMethod)
          .orElse(OPTIONS);

      String errorMsg;
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
        default:
          errorMsg = CommonConst.COMMON_OTHER_ERROR;
      }

      log.error("", e);
      apiResponseRvo = new ApiResponseRvo(errorMsg);
    } catch (Exception ex) {
      log.error("", ex);
    } finally {
      apiResponseRvo.setHttpStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
      apiResponseRvo.setHttpStatusCode(HttpStatus.BAD_REQUEST.value());
    }
    return apiResponseRvo;
  }

  @Getter
  enum RequestMethodEnum {

    M_GET(GetMapping.class, GET),
    M_POST(PostMapping.class, POST),
    M_PUT(PutMapping.class, PUT),
    M_DELETE(DeleteMapping.class, DELETE),
    M_GET_POST(QueryMapping.class, GET);

    private final Class clazz;
    private final RequestMethod method;

    RequestMethodEnum(Class clazz, RequestMethod method) {
      this.clazz = clazz;
      this.method = method;
    }

    public static RequestMethodEnum getEnum(Class clazz) {
      return Arrays.stream(values())
          .filter(e -> e.clazz.equals(clazz))
          .findAny()
          .orElse(null);
    }
  }

}
