package org.chun.uploadcc;

public interface IUploadCcService {

  String upload(UploadImageRequestBody requestBody);

  boolean exists(String qrcodeUrl);
}
