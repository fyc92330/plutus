package org.chun.plutus.common.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class LineUserDto {

  private String replyToken;

  private String userId;

  private String joinCode;

  private Long userNum;
}
