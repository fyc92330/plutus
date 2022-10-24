package org.chun.plutus.api.helper;

import org.springframework.beans.factory.annotation.Value;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chun.lineBot.ILineBotService;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class LineRichMenuHelper {

  private final ILineBotService lineBotService;

  @Value("${line.client.main-menu}")
  private String mainMenuId;
  @Value("${line.client.sub-menu}")
  private String subMenuId;

  public void mainMenuOnChange(String userId) {
    lineBotService.menuChange(userId, mainMenuId);
  }

  public void subMenuOnChange(String userId) {
    lineBotService.menuChange(userId, subMenuId);
  }

}
