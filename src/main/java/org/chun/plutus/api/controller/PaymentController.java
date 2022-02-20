package org.chun.plutus.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {

  @RequestMapping(value = "/payment", method = {GET})
  public Object queryPayments() {

    return null;
  }


}
