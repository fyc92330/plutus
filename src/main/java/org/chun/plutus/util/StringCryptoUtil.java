package org.chun.plutus.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.awt.geom.Arc2D.Float;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 字串資料加解密
 *
 * @author AJay
 */
public class StringCryptoUtil {
  private static final Logger log = LoggerFactory.getLogger(StringCryptoUtil.class);

  private static final String KEY = "5233112115177211";

  private static final byte[] DEFAULT_KEY_BYTE_ARY = KEY.getBytes();

  private static SecretKey LOCAL_SECRET_KEY = null;

  public static final String ENCRYPT_PREFIX_STR = "COM_IFTEK_";

  private static final String ALG = "AES";

  private static final Class<?>[] NO_SUPPORT_CLASS_ARY = {BigDecimal.class, Float.class, Double.class, Integer.class, Boolean.class};

  private static final String NO_SUPPORT_MSG = "Class of obj:@1@ is not support to FormatUtil.cryptoSpecifiedFieldOfOthers!!!";

  /**
   * @param paramString
   * @return
   */
  public static String getEncrypt(String paramString) {
    return getEncrypt(paramString, null);
  }

  /**
   * @param paramString
   * @param specifiedSecretKey
   * @return
   */
  public static String getEncrypt(String paramString, String specifiedSecretKey) {
    if (StringUtils.isNotBlank(paramString) && !paramString.startsWith(ENCRYPT_PREFIX_STR)) {
      byte[] arrayOfByte = encrypt(paramString, specifiedSecretKey);
      String str = parseByte2HexStr(arrayOfByte);
      return ENCRYPT_PREFIX_STR.concat(str);
    } else {
      return paramString;
    }
  }

  /**
   * @param paramString
   * @return
   */
  public static String getDecrypt(String paramString) throws Exception {
    return getDecrypt(paramString, null);
  }

  /**
   * @param paramString
   * @param specifiedSecretKey
   * @return
   */
  public static String getDecrypt(String paramString, String specifiedSecretKey) throws Exception {
    String resultString = paramString;
    if (StringUtils.isNotBlank(paramString) && paramString.startsWith(ENCRYPT_PREFIX_STR)) {
      String str = paramString.substring(ENCRYPT_PREFIX_STR.length(), paramString.length());
      byte[] arrayOfByte1 = parseHexStr2Byte(str);
      byte[] arrayOfByte2 = decrypt(arrayOfByte1, specifiedSecretKey);
      try {
        resultString = new String(arrayOfByte2, "UTF-8");
      } catch (UnsupportedEncodingException e) {
        log.error(e.getMessage(), e);
      }
    } else {
      throw new Exception(paramString + " DECRYPT FAIL!!!");
    }
    return resultString;
  }

  /**
   * @param paramArrayOfByte
   * @return
   */
  public static String parseByte2HexStr(byte[] paramArrayOfByte) {
    String hexStr = StringUtils.EMPTY;
    for (int i = 0; i < paramArrayOfByte.length; i++) {
      String str = Integer.toHexString(paramArrayOfByte[i] & 0xFF);
      if (str.length() == 1) {
        str = "0".concat(str);
      }
      hexStr = hexStr.concat(str.toUpperCase());
    }
    return hexStr;
  }

  /**
   * @param paramString
   * @return
   */
  public static byte[] encrypt(String paramString) {
    return encrypt(paramString, null);
  }

  /**
   * @param paramString
   * @param specifiedSecretKey
   * @return
   */
  public static byte[] encrypt(String paramString, String specifiedSecretKey) {
    try {
      SecretKeySpec localSecretKeySpec = getSecretKeySpec(specifiedSecretKey);
      byte[] arrayOfByte2 = paramString.getBytes(StandardCharsets.UTF_8);

      Cipher localCipher = Cipher.getInstance(ALG);
      localCipher.init(1, localSecretKeySpec);
      byte[] arrayOfByte3 = localCipher.doFinal(arrayOfByte2);
      return arrayOfByte3;
    } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
      localNoSuchAlgorithmException.printStackTrace();
    } catch (NoSuchPaddingException localNoSuchPaddingException) {
      localNoSuchPaddingException.printStackTrace();
    } catch (InvalidKeyException localInvalidKeyException) {
      localInvalidKeyException.printStackTrace();
    } catch (IllegalBlockSizeException localIllegalBlockSizeException) {
      localIllegalBlockSizeException.printStackTrace();
    } catch (BadPaddingException localBadPaddingException) {
      localBadPaddingException.printStackTrace();
    }
    return null;
  }

  /**
   * @return
   */
  private static SecretKeySpec getSecretKeySpec() {
    return getSecretKeySpec(null);
  }

  /**
   * @param specifiedSecretKey
   * @return
   */
  private static SecretKeySpec getSecretKeySpec(String specifiedSecretKey) {
    return getSecretKeySpec(specifiedSecretKey, false);
  }

  /**
   * @param specifiedSecretKey
   * @param isRandomKey
   * @return
   */
  private static SecretKeySpec getSecretKeySpec(String specifiedSecretKey, boolean isRandomKey) {
    byte[] keyByteAry = null;
    // 有指定key
    if (StringUtils.isNotBlank(specifiedSecretKey)) {
      keyByteAry = specifiedSecretKey.getBytes();
    } else {
      // 沒有指定key
      keyByteAry = DEFAULT_KEY_BYTE_ARY;
      // 用random key
      if (isRandomKey) {
        try {
          KeyGenerator localKeyGenerator = KeyGenerator.getInstance(ALG);
          localKeyGenerator.init(128, new SecureRandom(DEFAULT_KEY_BYTE_ARY));
          LOCAL_SECRET_KEY = localKeyGenerator.generateKey();
          keyByteAry = LOCAL_SECRET_KEY.getEncoded();
        } catch (NoSuchAlgorithmException e) {
          log.error(e.getMessage(), e);
        }
      }
    }
    return new SecretKeySpec(keyByteAry, ALG);
  }

  /**
   * @param paramString
   * @return
   */
  public static byte[] parseHexStr2Byte(String paramString) {
    if (paramString.length() < 1) {
      return null;
    }
    byte[] arrayOfByte = new byte[paramString.length() / 2];
    for (int i = 0; i < paramString.length() / 2; i++) {
      int j = Integer.parseInt(paramString.substring(i * 2, i * 2 + 1), 16);
      int k = Integer.parseInt(paramString.substring(i * 2 + 1, i * 2 + 2), 16);
      arrayOfByte[i] = (byte) (j * 16 + k);
    }
    return arrayOfByte;
  }

  /**
   * @param paramArrayOfByte
   * @return
   */
  public static byte[] decrypt(byte[] paramArrayOfByte) {
    return decrypt(paramArrayOfByte, null);
  }

  /**
   * @param paramArrayOfByte
   * @param specifiedSecretKey
   * @return
   */
  public static byte[] decrypt(byte[] paramArrayOfByte, String specifiedSecretKey) {
    try {
      SecretKeySpec localSecretKeySpec = getSecretKeySpec(specifiedSecretKey);
      Cipher localCipher = Cipher.getInstance(ALG);
      localCipher.init(2, localSecretKeySpec);
      byte[] arrayOfByte2 = localCipher.doFinal(paramArrayOfByte);
      return arrayOfByte2;
    } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
      localNoSuchAlgorithmException.printStackTrace();
    } catch (NoSuchPaddingException localNoSuchPaddingException) {
      localNoSuchPaddingException.printStackTrace();
    } catch (InvalidKeyException localInvalidKeyException) {
      localInvalidKeyException.printStackTrace();
    } catch (IllegalBlockSizeException localIllegalBlockSizeException) {
      localIllegalBlockSizeException.printStackTrace();
    } catch (BadPaddingException localBadPaddingException) {
      localBadPaddingException.printStackTrace();
    }
    return null;
  }

  public static String maskString(int prefixLength, int postfixLength, String paddingString, String str) {
    int end = Math.min(prefixLength, str.length());
    String prefixStr = StringUtils.substring(str, 0, end);
    int start = str.length() - Math.min(postfixLength, str.length());
    String postfixStr = StringUtils.substring(str, start, str.length());
    int paddingStrLength = str.length() - prefixStr.length() - postfixStr.length();
    String padding = paddingString;
    while (padding.length() < paddingStrLength) {
      padding = padding.concat(paddingString);
    }
    padding = StringUtils.substring(padding, 0, paddingStrLength);
    return prefixStr.concat(padding).concat(postfixStr);
  }

}