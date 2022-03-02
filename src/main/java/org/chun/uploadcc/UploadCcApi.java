package org.chun.uploadcc;

import okhttp3.ResponseBody;
import org.springframework.http.HttpHeaders;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UploadCcApi {

  @POST("/image_upload")
  Call<ResponseBody> upload(
      @Body UploadImageRequestBody requestBody,
      @Header(HttpHeaders.CONTENT_TYPE) String contentType);
}
