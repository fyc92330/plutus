package com.chun.line.client;

import com.chun.line.model.LineApiClientIdResponseBody;
import com.chun.line.model.LineApiProfileResponseBody;
import com.chun.line.model.LineApiTokenRequestBody;
import com.chun.line.model.LineApiTokenResponseBody;

public interface ILineLoginService {

  LineApiTokenResponseBody authorizationToken(LineApiTokenRequestBody tokenRequestBody, String contentType);

  LineApiClientIdResponseBody verify(String accessToken, String tokenType);

  LineApiProfileResponseBody profile(String accessToken, String tokenType);
}
