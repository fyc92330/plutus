package org.chun.lineBot;

import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import okhttp3.ResponseBody;
import org.springframework.http.HttpHeaders;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface LineBotApi {

  @GET("/v2/bot/profile")
  Call<ResponseBody> profile(
      @Header(HttpHeaders.AUTHORIZATION) String channelAccessToken,
      @Path("userId") String userId);

  @GET("/v2/bot/message/reply")
  Call<ResponseBody> reply(
      @Body ReplyMessage replyMessage,
      @Header(HttpHeaders.CONTENT_TYPE) String contentType,
      @Header(HttpHeaders.AUTHORIZATION) String channelAccessToken);

  @GET("/v2/bot/message/push")
  Call<ResponseBody> push(
      @Body PushMessage pushMessage,
      @Header(HttpHeaders.CONTENT_TYPE) String contentType,
      @Header(HttpHeaders.AUTHORIZATION) String channelAccessToken);

}
