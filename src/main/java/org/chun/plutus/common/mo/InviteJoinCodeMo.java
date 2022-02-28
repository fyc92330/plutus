package org.chun.plutus.common.mo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InviteJoinCodeMo {

  private String joinCode;

  private String actTitle;

  private String userName;

  private List<Long> userNumList;
}
