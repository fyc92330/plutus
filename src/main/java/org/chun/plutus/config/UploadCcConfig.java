package org.chun.plutus.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chun.plutus.config.properties.UploadCcProperties;
import org.chun.uploadcc.IUploadCcService;
import org.chun.uploadcc.UploadCcService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(UploadCcProperties.class)
public class UploadCcConfig {

  private final UploadCcProperties lineBotClientProperties;

  @Bean
  public IUploadCcService uploadCcService() {
    return new UploadCcService(lineBotClientProperties.getUrl());
  }
}
