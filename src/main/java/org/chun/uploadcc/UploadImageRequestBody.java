package org.chun.uploadcc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
@AllArgsConstructor
public class UploadImageRequestBody {

  private File uploadedFile;

  private String filename;

  private final String contentType = "image/png";
}
