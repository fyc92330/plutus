package org.chun.uploadcc;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.springframework.http.HttpHeaders;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

import java.io.File;
import java.util.List;
import java.util.Map;


public interface UploadCcApi {

  @Multipart
  @POST("/image_upload")
  Call<ResponseBody> upload(
      @Part MultipartBody.Part file,
      @Header(HttpHeaders.REFERER) String referer);

  @GET("/{url}")
  Call<ResponseBody> exists(@Path("url") String url);

  @Multipart
  @POST("/delete")
  Call<ResponseBody> delete(@PartMap List<DeleteImageRequestBody> deleteImageRequestBodyList);
}
