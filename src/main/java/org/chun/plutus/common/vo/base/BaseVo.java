package org.chun.plutus.common.vo.base;

import java.io.Serializable;

public abstract class BaseVo implements Serializable, Cloneable {

  public BaseVo() {
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
