package mx.jovannypcg.algo.p088_linkedlistcycle2;

import java.util.HashSet;
import java.util.Set;

/**
 * Given the {@code head} of a linked list, return the node where the cycle begins. If there is
 * no cycle, return {@code null}.
 *
 * <p>There is a cycle in a linked list if some node in the list can be reached again by
 * continuously following the {@code next} pointer. Internally, {@code pos} is used to denote
 * the index of the node that the tail's {@code next} pointer is connected to (0-indexed). It is
 * {@code -1} if there is no cycle. Note that {@code pos} is not passed as a parameter.
 *
 * <p>Do not modify the linked list.
 *
 * @see <a href="https://leetcode.com/problems/linked-list-cycle-ii/">Linked List Cycle II - LeetCode</a>
 */
public class Solution {

    public static class ListNode {

        int val;
        ListNode next;

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    public ListNode detectCycle(ListNode head) {
        if (head == null) return null;

        Set<ListNode> visited = new HashSet<>();
        ListNode current = head;

        while (current != null) {
            if (visited.contains(current)) return current;

            visited.add(current);

            current = current.next;
        }

        return null;
    }
}
