package org.chun.plutus;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.StringUtil;

import java.util.Arrays;
import java.util.List;

@DisplayName("LeetCode 測試")
public class LeetCodeTaskTest {

  @Test
  @DisplayName("LeetCode 主測試")
  @SneakyThrows
  void leetCode() {
    int[] nums = new int[]{0, 1, 2, 2, 3, 0, 4, 2};
    int val = 2;
    System.out.println(removeElement(nums, val));
  }

  public int removeElement(int[] nums, int val) {
    int[] results = new int[nums.length];
    int resultIndex = 0;
    for (int num : nums) {
      if (!(num == val)) {
        results[resultIndex] = num;
        resultIndex++;
      }
    }
    int[] rtns = new int[resultIndex];
    for (int i = 0; i < resultIndex; i++) {
      rtns[i] = results[i];
    }

    StringBuilder str = new StringBuilder();
    for (int i : rtns) {
      str.append(",");
      str.append(i);
    }
    System.out.println(str.substring(1));
    return resultIndex;
  }
}
