package org.chun.uploadcc;

import org.chun.uploadcc.model.RemoveImageRequestBody;
import org.chun.uploadcc.model.UploadImageRequestBody;
import org.chun.uploadcc.model.UploadImageResponseBody;

import java.util.List;

public interface IUploadCcService {

  UploadImageResponseBody upload(UploadImageRequestBody requestBody);

  boolean exists(String qrcodeUrl);

  boolean delete(List<RemoveImageRequestBody> removeImageRequestBodyList);
}
