package com.chun.line.client;

import com.chun.line.model.LineApiTokenRequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface LineLoginApi {

  @POST("/oauth2/v2.1/token")
  Call<ResponseBody> authorizationToken(@Body LineApiTokenRequestBody tokenRequestBody, @Header("Content-Type") String contentType);

  @GET("/oauth2/v2.1/verify")
  Call<ResponseBody> verify(@Path("access_token") String accessToken);

  @GET("/v2/profile")
  Call<ResponseBody> profile(@Path("access_token") String accessToken);

}
