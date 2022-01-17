package com.chun.line.client;

import com.chun.plutus.common.dto.LineAccessTokenDto;
import com.chun.plutus.common.dto.LineClientVerifyDto;

public interface ILineLoginService {

  LineClientVerifyDto authorizationToken(LineAccessTokenDto accessTokenDto, String contentType);

}
