package org.chun.plutus.api.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import org.chun.uploadcc.IUploadCcService;
import org.chun.uploadcc.UploadImageRequestBody;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageUploadHelper {

  private static final String QRCODE_FILE_NAME = "qrcodePic.png";

  private final IUploadCcService uploadCcService;

  /**
   * 上傳圖片
   *
   * @param os
   * @return
   */
  public String uploadImage(ByteArrayOutputStream os) {

    File qrcodeImageFile = new File(QRCODE_FILE_NAME);
    try (FileOutputStream fos = new FileOutputStream(qrcodeImageFile)) {
      os.writeTo(fos);
    } catch (IOException e) {
      log.info("image convert error.");
    }
    return uploadCcService.upload(new UploadImageRequestBody(qrcodeImageFile, QRCODE_FILE_NAME));
  }

  /**
   * 檢核路徑圖片存在
   *
   * @param qrcodeUrl
   * @return
   */
  public boolean qrcodeExists(String qrcodeUrl){
    return uploadCcService.exists(qrcodeUrl);
  }
}
