package com.chun.line.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LineApiClientIdResponseBody {

  private String clientId;

  private String scope;

  private Long expiresIn;
}
