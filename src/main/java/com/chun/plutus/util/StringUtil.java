package com.chun.plutus.util;

import java.util.Arrays;

public class StringUtil {

  public static String concat(Object... str) {
    StringBuilder sb = new StringBuilder();
    Arrays.stream(str).map(String::valueOf).forEach(sb::append);
    return sb.toString();
  }
}
