package org.chun.plutus.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/record")
public class PaymentRecordController {

  @GetMapping("/init/{userNum}")
  public Object init(@PathVariable Long userNum){
    return null;
  }

  @PostMapping("/payment")
  public void savePaymentRecord(){

  }

  @PutMapping("/payment")
  public void editPaymentRecord(){

  }

  @DeleteMapping("/payment")
  public void removePaymentRecord(){

  }

}
