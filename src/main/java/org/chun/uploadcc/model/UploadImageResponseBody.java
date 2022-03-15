package org.chun.uploadcc.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class UploadImageResponseBody {

  private Integer code;

  private Integer totalError;

  private Integer totalSuccess;

  private List<ImageBody> successImage;

  private String url;

}
