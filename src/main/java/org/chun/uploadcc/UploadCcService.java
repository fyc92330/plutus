package org.chun.uploadcc;

import com.linecorp.bot.model.profile.UserProfileResponse;
import okhttp3.ResponseBody;
import org.chun.lineBot.LineBotConfig;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

import static org.chun.uploadcc.UploadCcConfig.OBJECT_MAPPER;

public class UploadCcService implements IUploadCcService {

  private static final String CONTENT_TYPE = "multipart/form-data";
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
  public String upload(UploadImageRequestBody requestBody) {
    Call<ResponseBody> call = uploadCcApi.upload(requestBody, CONTENT_TYPE);
    UploadImageResponseBody response = convertResult(call, UploadImageResponseBody.class);
    return response.getSuccessImage().stream()
        .findAny()
        .map(UploadImageResponseBody.Image::getUrl)
        .map(baseUrl::concat)
        .get();
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
