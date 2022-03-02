package org.chun.uploadcc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UploadImageRequestBody {

  private byte[] uploadedFile;

}
