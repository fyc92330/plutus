package org.chun.plutus.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;

public class QrcodeUtil {

  /**
   * 產生Qrcode圖
   *
   * @param code
   * @return barcode ByteArrayOutputStream
   * @throws Exception
   */
  public static ByteArrayOutputStream generateQrcode(String code) throws Exception {

    try (ByteArrayOutputStream bout = new ByteArrayOutputStream(4096)) {

      QRCodeWriter qrCodeWriter = new QRCodeWriter();
      BitMatrix bitMatrix = qrCodeWriter.encode(code, BarcodeFormat.QR_CODE, 250, 250);
      MatrixToImageWriter.writeToStream(bitMatrix, "PNG", bout);

      return bout;
    }
  }
}
