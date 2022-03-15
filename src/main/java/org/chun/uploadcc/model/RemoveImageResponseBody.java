package org.chun.uploadcc.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class RemoveImageResponseBody {

  private Integer code;

  private Integer totalSuccess;

  private List<ImageBody> successImage;

}
