package com.chun.plutus.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LineAccessTokenDto {

  private String accessToken;

  private Long expiresIn;

  private String idToken;

  private String refreshToken;

  private String scope;

  private String tokenType;

}
