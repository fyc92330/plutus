package org.chun.plutus;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TaskTest {

  @Test
  void task() {

  }

  private ListNode c(ListNode list1, ListNode list2) {
    ListNode ln = new ListNode(list1.val, list2);
    return list1.stream().sorted(Comparator.comparing(n -> n.val)).collect(Collectors.toList());
  }

  public class ListNode {
    int val;
    ListNode next;

    ListNode() {
    }

    ListNode(int val) {
      this.val = val;
    }

    ListNode(int val, ListNode next) {
      this.val = val;
      this.next = next;
    }
  }
}
