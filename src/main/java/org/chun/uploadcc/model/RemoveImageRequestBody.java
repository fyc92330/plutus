package org.chun.uploadcc.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RemoveImageRequestBody {

  private String path;

  private String key;
}
