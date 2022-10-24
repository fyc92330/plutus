package org.chun.uploadcc;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.apache.logging.log4j.util.Strings;
import org.chun.plutus.util.StringUtil;
import org.chun.uploadcc.model.ImageBody;
import org.chun.uploadcc.model.RemoveImageRequestBody;
import org.chun.uploadcc.model.RemoveImageResponseBody;
import org.chun.uploadcc.model.UploadImageRequestBody;
import org.chun.uploadcc.model.UploadImageResponseBody;
import org.springframework.http.HttpStatus;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.chun.uploadcc.UploadCcConfig.OBJECT_MAPPER;

public class UploadCcService implements IUploadCcService {

  private static final String FORM_DATA_NAME = "uploaded_file[]";
  private static final String IMAGE_CONTENT_TYPE = "image/png";
  private static final String DATA_CONTENT_TYPE = "multipart/form-data";
  private final UploadCcApi uploadCcApi;
  private final String baseUrl;

  public UploadCcService(String baseUrl) {
    this.baseUrl = baseUrl;
    this.uploadCcApi = new Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(JacksonConverterFactory.create(OBJECT_MAPPER))
        .build()
        .create(UploadCcApi.class);
  }

  @Override
  public UploadImageResponseBody upload(UploadImageRequestBody uploadImageRequestBody) {
    RequestBody requestBody = RequestBody.create(MediaType.parse(IMAGE_CONTENT_TYPE), uploadImageRequestBody.getUploadedFile());
    MultipartBody.Part body = MultipartBody.Part.createFormData(FORM_DATA_NAME, uploadImageRequestBody.getFilename(), requestBody);
    Call<ResponseBody> call = uploadCcApi.upload(body, baseUrl);
    UploadImageResponseBody responseBody = convertResult(call, UploadImageResponseBody.class);
    responseBody.getSuccessImage().stream()
        .findAny()
        .map(ImageBody::getUrl)
        .map(url -> StringUtil.concat(baseUrl, "/", url))
        .ifPresent(responseBody::setUrl);
    return responseBody;
  }

  @Override
  public boolean exists(String qrcodeUrl) {
    if (qrcodeUrl == null) {
      return false;
    }
    final String url = qrcodeUrl.replace(baseUrl, Strings.EMPTY).substring(1);
    try {
      Call<ResponseBody> call = uploadCcApi.exists(url);
      Response<ResponseBody> response = call.execute();
      if (HttpStatus.NOT_FOUND.value() == response.code()) {
        throw new RuntimeException(HttpStatus.NOT_FOUND.getReasonPhrase());
      }
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  @Override
  public boolean delete(List<RemoveImageRequestBody> removeImageRequestBodyList) {
    List<RequestBody> requestBodyList = removeImageRequestBodyList.stream()
        .map(RemoveImageRequestBody::toString)
        .map(jsonStr -> RequestBody.create(MediaType.parse(DATA_CONTENT_TYPE), jsonStr))
        .collect(Collectors.toList());
    MultipartBody.Part body = MultipartBody.Part.createFormData("key", requestBodyList.toString());
    Call<ResponseBody> call = uploadCcApi.delete(body);
    convertResult(call, RemoveImageResponseBody.class);
    return true;
  }

  /** =================================================== private ================================================== */

  private <T> T convertResult(Call<ResponseBody> call, Class<T> resultType) {
    String responseBody;
    try {
      Response<ResponseBody> response = call.execute();
      responseBody = response.body().string();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return convertResult(responseBody, resultType);
  }

  private <T> T convertResult(String json, Class<T> resultType) {
    try {
      return OBJECT_MAPPER.readValue(json, resultType);
    } catch (Exception e) {
      throw new RuntimeException(String.format("convertResult error. json: %s, resultType: %s", json, resultType), e);
    }
  }
}
