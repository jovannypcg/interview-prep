package mx.jovannypcg.algo.p089_reorderlist;

/**
 * You are given the {@code head} of a singly linked list. The list can be represented as:
 *
 * <p>{@code L0 -> L1 -> ... -> Ln-1 -> Ln}
 *
 * <p>Reorder the list to be on the following form:
 *
 * <p>{@code L0 -> Ln -> L1 -> Ln-1 -> L2 -> Ln-2 -> ...}
 *
 * <p>You may not modify the values in the list's nodes. Only nodes themselves may be changed.
 *
 * @see <a href="https://leetcode.com/problems/reorder-list/">Reorder List - LeetCode</a>
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

    public void reorderList(ListNode head) {
        if (head == null) return;

        // Starting fast at head.next makes slow land on the middle node (odd length)
        // or the last node of the first half (even length).
        ListNode slow = head;
        ListNode fast = head.next;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        // Split into two halves. This also null-terminates the eventual merged list,
        // since the middle node (slow) ends up last after merging.
        ListNode second = slow.next;
        slow.next = null;

        // Reverse the second half so it can be walked front-to-back, like the first.
        ListNode prev = null;
        while (second != null) {
            ListNode temp = second.next;
            second.next = prev;
            prev = second;
            second = temp;
        }

        // Weave the two halves together, alternating nodes from each.
        ListNode first = head;
        second = prev; // head of the reversed second half

        while (second != null) {
            ListNode temp1 = first.next, temp2 = second.next;

            first.next = second;
            second.next = temp1;
            first = temp1;
            second = temp2;
        }
    }
}
