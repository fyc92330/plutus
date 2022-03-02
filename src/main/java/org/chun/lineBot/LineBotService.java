package org.chun.lineBot;

import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.error.ErrorResponse;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.profile.UserProfileResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import org.chun.plutus.common.constant.LineApiResponseMessageConst;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;

import static org.chun.lineBot.LineBotConfig.OBJECT_MAPPER;

@Slf4j
public class LineBotService implements ILineBotService {

  private static final String CONTENT_TYPE = "application/json";
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
  public void reply(ReplyMessage replyMessage, String userId) {
    Call<ResponseBody> call = lineBotApi.reply(replyMessage, CONTENT_TYPE, channelAccessToken);
    sendMessage(call, replyMessage.getMessages(), userId);
  }

  @Override
  public void push(PushMessage pushMessage) {
    Call<ResponseBody> call = lineBotApi.push(pushMessage, CONTENT_TYPE, channelAccessToken);
    sendMessage(call);
  }

  /** =================================================== private ================================================== */

  public void sendMessage(Call<ResponseBody> call, List<Message> messages, String userId) {
    if (sendMessage(call)) {
      log.info("reply message is sending success.");
    } else {
      PushMessage pushMessage = new PushMessage(userId, messages);
      push(pushMessage);
    }
  }


  private boolean sendMessage(Call<ResponseBody> call) {
    ErrorResponse error;
    try {
      Response<ResponseBody> response = call.execute();
      assert response.body() != null;
      String responseBody = response.body().string();
      if (LineApiResponseMessageConst.SUCCESS_RESPONSE_BODY.equals(responseBody)) return true;
      error = convertResult(responseBody, ErrorResponse.class);
    } catch (Exception e) {
      throw new RuntimeException();
    }

    assert error != null;
    final String errorMessage = error.getMessage();
    if (LineApiResponseMessageConst.INVALID_REPLY_TOKEN.equals(errorMessage)) {
      return false;
    } else if (LineApiResponseMessageConst.FAILED_TO_SEND_MESSAGES.equals(errorMessage)) {
      throw new RuntimeException(errorMessage);
    } else {
      throw new RuntimeException();
    }

  }

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
