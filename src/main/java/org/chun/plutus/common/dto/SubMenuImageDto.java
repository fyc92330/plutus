package org.chun.plutus.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SubMenuImageDto {

  private String joinCode;

  private String titleImageUrl;

  private String costImageUrl;

  private String daddyImageUrl;

  private String typeImageUrl;
}
