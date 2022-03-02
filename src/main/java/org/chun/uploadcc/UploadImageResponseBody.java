package org.chun.uploadcc;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UploadImageResponseBody {

  private Integer code;

  private Integer totalError;

  private Integer totalSuccess;

  private List<Image> successImage;

  @Getter
  @Setter
  static class Image {
    private String delete;
    private String name;
    private String thumbnail;
    private String url;
  }
}
