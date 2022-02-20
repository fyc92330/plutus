package org.chun.plutus.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

/**
 * Token 相關
 */
public class TokenUtil {

  private static final String PRIFIX_BEARER = "Bearer ";

  private static String SECRET;

  public TokenUtil(String secret) {
    SECRET = secret;
  }

  /**
   * generate JWT
   *
   * @param userNum
   * @param userInfoJsonStr
   */
  public static String genJwtToken(String userInfoJsonStr, Long userNum) throws JsonProcessingException {
    final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    final Date now = new Date(System.currentTimeMillis());

    /** sign with secret */
    byte[] apiKeySecretBytes = Base64.getEncoder().encode(SECRET.getBytes());
    final Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
    /** Encrypt UserObject Before Set JWT */
    return Jwts.builder()
        .setId(String.valueOf(userNum))
        .setIssuedAt(now)
        .setSubject(StringCryptoUtil.getEncrypt(userInfoJsonStr))
        .signWith(signatureAlgorithm, signingKey)
        .compact();
  }

  /**
   * parse JWT
   */
  public static String parseJwtToken(String jwtToken) {
    /** sign with secret */
    final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    byte[] apiKeySecretBytes = Base64.getEncoder().encode(SECRET.getBytes());
    final Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

    /** Decode jwtToken to Json */
    try {
      return Jwts.parser()
          .setSigningKey(signingKey)
          .parseClaimsJws(jwtToken)
          .getBody()
          .getSubject();
    } catch (Exception e) {
    }
    return null;
  }

  /**
   * generate AccessToken for header
   */
  public static String genAccessToken(final String token) {
    return PRIFIX_BEARER.concat(token);
  }

}
