package com.chun.line.client;

import com.chun.line.model.LineApiClientIdResponseBody;
import com.chun.line.model.LineApiProfileResponseBody;
import com.chun.line.model.LineApiTokenRequestBody;
import com.chun.line.model.LineApiTokenResponseBody;
import com.chun.plutus.common.dto.LineAccessTokenDto;
import com.chun.plutus.common.dto.LineClientVerifyDto;
import com.chun.plutus.util.StringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.UUID;

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
  public LineApiTokenResponseBody authorizationToken(LineApiTokenRequestBody tokenRequestBody, String contentType) {
    final String nonce = UUID.randomUUID().toString();
    Call<ResponseBody> call = lineLoginApi.authorizationToken(tokenRequestBody, contentType);
    return convertResult(call, LineApiTokenResponseBody.class);
  }

  @Override
  public LineApiClientIdResponseBody verify(String accessToken, String tokenType) {
    final String token = StringUtil.concat(tokenType, " ", accessToken);
    Call<ResponseBody> call = lineLoginApi.verify(token);
    return convertResult(call, LineApiClientIdResponseBody.class);
  }

  @Override
  public LineApiProfileResponseBody profile(String accessToken, String tokenType) {
    final String token = StringUtil.concat(tokenType, " ", accessToken);
    Call<ResponseBody> call = lineLoginApi.profile(token);
    return convertResult(call, LineApiProfileResponseBody.class);  }

  private <T> T convertResult(Call<ResponseBody> call, Class<T> resultType) {
    Response<ResponseBody> response = execute(call);
    log.info("response:{}", response);
    String json = readJsonString(response);
    log.info("json:{}", json);
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
