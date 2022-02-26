package org.chun.plutus.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chun.lineBot.ILineBotService;
import org.chun.lineBot.LineBotService;
import org.chun.plutus.config.properties.LineBotClientProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(LineBotClientProperties.class)
public class LineApiConfig {

  private final LineBotClientProperties lineBotClientProperties;

  @Bean
  public ILineBotService lineBotService() {
    return new LineBotService(lineBotClientProperties.getApiUrl(), lineBotClientProperties.getAccessToken());
  }
}
