package org.chun.plutus.util;

import java.util.Arrays;
import java.util.function.IntSupplier;

public class DaoValidationUtil {

  /**
   * 檢核執行結果為一筆
   *
   * @param action
   * @param paramArgs
   */
  public static void validateResultIsOne(IntSupplier action, Object... paramArgs) {
    int count = action.getAsInt();
    if (count != 1) {
      String classNameString = Arrays.toString(Arrays.stream(paramArgs).map(obj -> obj.getClass().getSimpleName()).toArray());
      throw new RuntimeException(String.format("RESULT MUST BE ONE,count:%s, class:%s, param:%s", count, classNameString, Arrays.toString(paramArgs)));
    }
  }

  /**
   * 檢核執行筆數與預期相同
   *
   * @param action
   * @param expect
   * @param paramArgs
   */
  public static void validateResultCount(IntSupplier action, int expect, Object... paramArgs) {
    int count = action.getAsInt();
    if (count != expect) {
      String classNameString = Arrays.toString(Arrays.stream(paramArgs).map(obj -> obj.getClass().getSimpleName()).toArray());
      throw new RuntimeException(String.format("RESULT COUNT NOT AS EXPECTED:count:%s, expect:%s, class:%s, param:%s", count, expect, classNameString, Arrays.toString(paramArgs)));
    }
  }
}
