package com.chun.plutus.config.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "line.login")
public class LineLoginProperties {

  @NotEmpty(message = "")
  private String apiUri;

  @NotEmpty(message = "")
  private String loginPageUri;

  @NotEmpty(message = "")
  private String clientId;

  @NotEmpty(message = "")
  private String scope;

  @NotEmpty(message = "")
  private String redirectUri;

}
