package org.chun.line.client;

import org.chun.line.model.LineApiClientIdResponse;
import org.chun.line.model.LineApiProfileResponse;
import org.chun.plutus.util.StringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Slf4j
public class LineLoginService implements ILineLoginService {

  private static final String CONTENT_TYPE = "application/json";
  private static final ObjectMapper OBJECT_MAPPER = LineClientConfig.OBJECT_MAPPER;
  private final LineLoginApi lineLoginApi;

  public LineLoginService(String baseUrl) {
    lineLoginApi = new Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(JacksonConverterFactory.create(OBJECT_MAPPER))
        .build()
        .create(LineLoginApi.class);
  }

  @Override
  public LineApiClientIdResponse verify(String accessToken, String tokenType) {
    final String token = StringUtil.concat(tokenType, " ", accessToken);
    Call<ResponseBody> call = lineLoginApi.verify(token);
    return convertResult(call, LineApiClientIdResponse.class);
  }

  @Override
  public LineApiProfileResponse profile(String accessToken, String tokenType) {
    final String token = StringUtil.concat(tokenType, " ", accessToken);
    Call<ResponseBody> call = lineLoginApi.profile(token);
    return convertResult(call, LineApiProfileResponse.class);
  }

  /** =================================================== private ================================================== */

  private <T> T convertResult(Call<ResponseBody> call, Class<T> resultType) {
    Response<ResponseBody> response = execute(call);
    String json = readJsonString(response);
    return convertResult(json, resultType);

  }

  private Response<ResponseBody> execute(Call<ResponseBody> call) {
    try {
      return call.execute();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private String readJsonString(Response<ResponseBody> response) {
    try {
      return response.body().string();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private <T> T convertResult(String json, Class<T> resultType) {
    try {
      return OBJECT_MAPPER.readValue(json, resultType);
    } catch (Exception e) {
      throw new RuntimeException(String.format("convertResult error. json: %s, resultType: %s", json, resultType), e);
    }
  }
}
