package com.chun.line.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class LineApiTokenResponse {

  private String accessToken;

  private Long expiresIn;

  private String idToken;

  private String refreshToken;

  private String scope;

  private String tokenType;
}
