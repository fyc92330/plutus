package com.chun.plutus.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class OverSpendEvent {

  /** 餘額 */
  private BigDecimal balance;
}
