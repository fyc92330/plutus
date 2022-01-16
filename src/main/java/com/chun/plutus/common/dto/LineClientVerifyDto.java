package com.chun.plutus.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LineClientVerifyDto {

  private String scope;

  private String clientId;

  private Long expiresIn;

}
