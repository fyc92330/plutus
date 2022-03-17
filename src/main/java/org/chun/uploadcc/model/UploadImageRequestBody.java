package org.chun.uploadcc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.File;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class UploadImageRequestBody {

  private File uploadedFile;

  private String filename;

  private String contentType;
}
