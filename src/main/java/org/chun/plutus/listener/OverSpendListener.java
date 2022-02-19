package org.chun.plutus.listener;

import org.chun.plutus.event.OverSpendEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class OverSpendListener {

  @Async
  @EventListener(condition = "#overSpendEvent.balance != null")
  public void userOverSpend(OverSpendEvent overSpendEvent){

  }
}
