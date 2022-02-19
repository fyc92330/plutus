package org.chun.plutus.config;

import org.chun.line.client.ILineLoginService;
import org.chun.line.client.LineLoginService;
import org.chun.plutus.config.properties.LineClientProperties;
import org.chun.plutus.config.properties.LineLoginProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({LineLoginProperties.class, LineClientProperties.class})
public class LineApiConfig {

  private final LineLoginProperties lineLoginProperties;
  private final LineClientProperties lineClientProperties;

  @Bean(name = "lineLoginService")
  public ILineLoginService lineLoginService() {
    return new LineLoginService(lineLoginProperties.getApiUri());
  }


}
