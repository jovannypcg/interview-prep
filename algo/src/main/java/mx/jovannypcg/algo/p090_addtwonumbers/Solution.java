package mx.jovannypcg.algo.p090_addtwonumbers;

/**
 * You are given two non-empty linked lists representing two non-negative integers. The digits
 * are stored in reverse order, and each of their nodes contains a single digit. Add the two
 * numbers and return the sum as a linked list.
 *
 * <p>You may assume the two numbers do not contain any leading zero, except the number 0 itself.
 *
 * @see <a href="https://leetcode.com/problems/add-two-numbers/">Add Two Numbers - LeetCode</a>
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

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        if (l1 == null) return l2;
        if (l2 == null) return l1;

        ListNode out = new ListNode(-1); // dummy-node pattern
        ListNode currOut = out,
            currL1 = l1,
            currL2 = l2;

        int carry = 0;

        // Phase 1: add digit-by-digit while both lists still have nodes.
        while (currL1 != null && currL2 != null) {
            int sum = currL1.val + currL2.val + carry;
            int res = sum % 10;
            carry = sum / 10;

            currOut.next = new ListNode(res);
            currOut = currOut.next;

            currL1 = currL1.next;
            currL2 = currL2.next;
        }

        // Phase 2: l1 is longer — keep adding its remaining digits + carry.
        while (currL1 != null) {
            int sum = currL1.val + carry;
            int res = sum % 10;
            carry = sum / 10;

            currOut.next = new ListNode(res);
            currOut = currOut.next;

            currL1 = currL1.next;
        }

        // Phase 3: l2 is longer — keep adding its remaining digits + carry.
        while (currL2 != null) {
            int sum = currL2.val + carry;
            int res = sum % 10;
            carry = sum / 10;

            currOut.next = new ListNode(res);
            currOut = currOut.next;

            currL2 = currL2.next;
        }

        // Leftover carry becomes one final extra node (e.g. 5 + 5 = 10).
        if (carry != 0) {
            currOut.next = new ListNode(carry);
        }

        return out.next;
    }
}
