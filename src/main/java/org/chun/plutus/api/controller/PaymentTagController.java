package org.chun.plutus.api.controller;

import org.chun.plutus.common.vo1.PaymentTagGroupVo;
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
@RequestMapping(value = "/api/tag")
public class PaymentTagController {

  @GetMapping("/init")
  public Object init() {

    return null;
  }

  @GetMapping("/tags/{userNum}")
  public Object queryCustomTagGroup(@PathVariable Long userNum) {
    PaymentTagGroupVo paymentTagGroupVo = new PaymentTagGroupVo();
    return paymentTagGroupVo;
  }

  @PostMapping("/tags")
  public void createTagGroup(PaymentTagGroupVo paymentTagGroupVo) {

  }

  @PutMapping("/tags")
  public void updateTagGroup() {

  }

  @DeleteMapping("/tags")
  public void removeTagGroup() {

  }

  @PostMapping("/tag")
  public void createTag() {

  }

  @PutMapping("/tag")
  public void updateTag() {

  }

  @DeleteMapping("/tag")
  public void removeTag() {

  }


}
