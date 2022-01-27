package com.chun.line.client;

import com.chun.line.model.LineApiClientIdResponse;
import com.chun.line.model.LineApiProfileResponse;

public interface ILineLoginService {

  LineApiClientIdResponse verify(String accessToken, String tokenType);

  LineApiProfileResponse profile(String accessToken, String tokenType);
}
