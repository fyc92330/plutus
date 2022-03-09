package org.chun.plutus.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class JoinCodeDto {

  private String replyToken;

  private String userId;

  private String joinCode;

  private Long userNum;
}
