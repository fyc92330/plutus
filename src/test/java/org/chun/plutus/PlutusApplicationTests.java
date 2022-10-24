package org.chun.plutus;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class PlutusApplicationTests {

  @Test
  @DisplayName("內部測試")
  void contextLoads() {
    int[] arr = new int[]{1, 1, 2};
    System.out.printf("answer:(%d)", q26(arr));
    System.out.println(RandomStringUtils.random(6, true, true));
  }

  private int q26(int[] nums) {
    int len = nums.length;
    List<Integer> numList = new ArrayList<>();
    for (int i : nums) {
      if (!numList.contains(i)) {
        numList.add(i);
      }
    }

    for (int j = 0; j < numList.size(); j++) {
      nums[j] = numList.get(j);
    }

    for (int k = numList.size(); k < len; k++) {
      nums[k] = 0;
    }

    return numList.size();
  }

}
