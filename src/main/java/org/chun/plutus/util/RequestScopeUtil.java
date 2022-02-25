package org.chun.plutus.util;

import org.chun.plutus.common.constant.RequestScopeConst;
import org.springframework.web.context.request.RequestContextHolder;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

public class RequestScopeUtil {

  public static Long getUserNum() {
    return (Long) RequestContextHolder.getRequestAttributes().getAttribute(RequestScopeConst.USER_NUM_STR, SCOPE_REQUEST);
  }
}
