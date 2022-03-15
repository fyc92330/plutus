package org.chun.plutus.api.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chun.plutus.common.dto.SubMenuImageDto;
import org.chun.uploadcc.IUploadCcService;
import org.chun.uploadcc.model.UploadImageRequestBody;
import org.chun.uploadcc.model.UploadImageResponseBody;
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
  private static final String TITLE_FILE_NAME = "title.png";
  private static final String COST_FILE_NAME = "cost.png";
  private static final String DADDY_FILE_NAME = "daddy.png";
  private static final String TYPE_FILE_NAME = "type.png";
  private static final String PNG_CONTENT_TYPE = "image/png";
  private static final String JPG_CONTENT_TYPE = "image/jpg";


  private final IUploadCcService uploadCcService;

  /**
   * 上傳圖片
   *
   * @param os
   * @return
   */
  public UploadImageResponseBody uploadImage(ByteArrayOutputStream os) {

    File qrcodeImageFile = new File(QRCODE_FILE_NAME);
    try (FileOutputStream fos = new FileOutputStream(qrcodeImageFile)) {
      os.writeTo(fos);
    } catch (IOException e) {
      log.info("image convert error.");
    }
    return uploadCcService.upload(new UploadImageRequestBody(qrcodeImageFile, QRCODE_FILE_NAME, PNG_CONTENT_TYPE));
  }

  /**
   * 檢核路徑圖片存在
   *
   * @param qrcodeUrl
   * @return
   */
  public boolean qrcodeExists(String qrcodeUrl) {
    return uploadCcService.exists(qrcodeUrl);
  }

  /**
   * 上傳子選單所需圖片
   *
   * @param joinCode
   * @return
   */
  public SubMenuImageDto genSubMenuDto(String joinCode) {
    final String titleImageUrl = uploadCcService.upload(
        new UploadImageRequestBody(new File(TITLE_FILE_NAME), TITLE_FILE_NAME, PNG_CONTENT_TYPE)).getUrl();
    final String costImageUrl = uploadCcService.upload(
        new UploadImageRequestBody(new File(COST_FILE_NAME), COST_FILE_NAME, PNG_CONTENT_TYPE)).getUrl();
    final String daddyImageUrl = uploadCcService.upload(
        new UploadImageRequestBody(new File(DADDY_FILE_NAME), DADDY_FILE_NAME, PNG_CONTENT_TYPE)).getUrl();
    final String typeImageUrl = uploadCcService.upload(
        new UploadImageRequestBody(new File(TYPE_FILE_NAME), TYPE_FILE_NAME, PNG_CONTENT_TYPE)).getUrl();
    return new SubMenuImageDto(joinCode, titleImageUrl, costImageUrl, daddyImageUrl, typeImageUrl);
  }
}
