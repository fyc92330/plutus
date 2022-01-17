package com.chun.plutus.config.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "line.client")
public class LineClientProperties {

  @NotEmpty
  private String id;

  @NotEmpty
  private String secret;

}
