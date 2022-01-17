package com.chun.line.client;

import com.chun.plutus.common.dto.LineAccessTokenDto;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface LineLoginApi {

  @POST("/oauth2/v2.1/token")
  Call<ResponseBody> authorizationToken(@Body LineAccessTokenDto accessTokenDto, @Header("Content-Type") String contentType);
}
