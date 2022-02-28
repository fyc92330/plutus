package org.chun.plutus.common.vo;

import lombok.Getter;
import lombok.Setter;
import org.chun.plutus.common.vo.base.ActivityBasicBaseVo;

import java.io.Serializable;

/**
 * For extending functions, edit this file please.
 */
@Getter
@Setter
public class ActivityBasicVo extends ActivityBasicBaseVo implements Serializable, Cloneable {

  public ActivityBasicVo() {
    super();
  }

  private String hostUserName;

}
