package com.chun.line.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class LineApiTokenRequest {

  private String grantType;

  private String code;

  private String redirectUri;

  private String clientId;

  private String clientSecret;
}
