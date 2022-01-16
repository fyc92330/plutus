package com.chun.plutus.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LineUserProfileDto {

  private String userId;

  private String displayName;

  private String pictureUrl;

  private String statusMessage;
}
