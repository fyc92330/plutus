package org.chun.line.client;

import org.chun.line.model.LineApiClientIdResponse;
import org.chun.line.model.LineApiProfileResponse;

public interface ILineLoginService {

  LineApiClientIdResponse verify(String accessToken, String tokenType);

  LineApiProfileResponse profile(String accessToken, String tokenType);
}
