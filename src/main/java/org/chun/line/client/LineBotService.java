package org.chun.line.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.error.ErrorResponse;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.profile.UserProfileResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.List;

public class LineBotService implements ILineBotService {

  private static final String CONTENT_TYPE = "application/json";
  private static final ObjectMapper OBJECT_MAPPER = LineBotConfig.OBJECT_MAPPER;
  private static final String TOKEN_PREFIX = "Bearer ";
  private final LineBotApi lineBotApi;
  private final String channelAccessToken;

  public LineBotService(String baseUrl, String channelAccessToken) {
    this.channelAccessToken = TOKEN_PREFIX.concat(channelAccessToken);
    this.lineBotApi = new Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(JacksonConverterFactory.create(OBJECT_MAPPER))
        .build()
        .create(LineBotApi.class);
  }

  @Override
  public UserProfileResponse profile(String userId) {
    Call<ResponseBody> call = lineBotApi.profile(channelAccessToken, userId);
    return convertResult(call, UserProfileResponse.class);
  }

  @Override
  public ErrorResponse reply(ReplyMessage replyMessage, String userId) {
    Call<ResponseBody> call = lineBotApi.reply(replyMessage, CONTENT_TYPE, channelAccessToken);
    ErrorResponse error = convertResult(call, ErrorResponse.class);
    return error.getMessage() != null
        ? convertPushMessage(userId, replyMessage.getMessages())
        : null;
  }

  @Override
  public ErrorResponse push(PushMessage pushMessage) {
    Call<ResponseBody> call = lineBotApi.push(pushMessage, CONTENT_TYPE, channelAccessToken);
    ErrorResponse error = convertResult(call, ErrorResponse.class);
    return error.getMessage() != null
        ? error
        : null;
  }

  /** =================================================== private ================================================== */

  private ErrorResponse convertPushMessage(String userId, List<Message> messageList) {
    PushMessage pushMessage = new PushMessage(userId, messageList);
    return this.push(pushMessage);
  }

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
