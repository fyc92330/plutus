package com.chun.line.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LineApiTokenRequestBody {

  private String grantType;

  private String code;

  private String redirectUri;

  private String clientId;

  private String clientSecret;
}
