package com.chun.line.client;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface LineLoginApi {

  @GET("/oauth2/v2.1/verify")
  Call<ResponseBody> verify(@Path("access_token") String accessToken);

  @GET("/v2/profile")
  Call<ResponseBody> profile(@Path("access_token") String accessToken);

}
