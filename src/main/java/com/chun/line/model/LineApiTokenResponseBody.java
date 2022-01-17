package com.chun.line.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LineApiTokenResponseBody {

  private String accessToken;

  private Long expiresIn;

  private String idToken;

  private String refreshToken;

  private String scope;

  private String tokenType;
}
