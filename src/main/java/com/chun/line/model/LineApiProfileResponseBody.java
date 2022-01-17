package com.chun.line.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LineApiProfileResponseBody {

  private String userId;

  private String displayName;

  private String pictureUrl;

  private String statusMessage;
}
