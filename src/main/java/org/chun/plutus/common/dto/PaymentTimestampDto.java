package org.chun.plutus.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class PaymentTimestampDto {

  private Long userTimestamp;

  private Long totalTimestamp;
}
