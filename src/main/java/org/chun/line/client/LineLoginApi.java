package org.chun.line.client;

import okhttp3.ResponseBody;
import org.springframework.http.HttpHeaders;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LineLoginApi {

  @GET("/oauth2/v2.1/verify")
  Call<ResponseBody> verify(@Query("access_token") String accessToken);

  @GET("/v2/profile")
  Call<ResponseBody> profile(@Header(HttpHeaders.AUTHORIZATION) String accessToken);

}
