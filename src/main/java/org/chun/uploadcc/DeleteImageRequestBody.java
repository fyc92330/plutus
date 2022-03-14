package org.chun.uploadcc;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DeleteImageRequestBody {

  private String path;

  private String key;
}
