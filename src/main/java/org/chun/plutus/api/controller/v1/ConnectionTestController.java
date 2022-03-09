package org.chun.plutus.api.controller.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chun.plutus.common.dao.PaymentUserDao;
import org.chun.plutus.common.vo.PaymentUserVo;
import org.chun.plutus.util.MapUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/unauth/connect")
public class ConnectionTestController {

  private static final String successMsg = "user is exists.";
  private static final String failMsg = "connection is fail.";

  private final PaymentUserDao paymentUserDao;

  @GetMapping("/local")
  public Object databaseConnectionTest() {
    final PaymentUserVo userVo = paymentUserDao.listAll().stream().findAny().orElse(null);
    log.info("{}", userVo);
    return MapUtil.newHashMap("result", userVo == null ? failMsg : successMsg);
  }
}
