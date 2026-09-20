package mx.jovannypcg.algo.p018_removenthnode;

/**
 * Given the head of a linked list, remove the nth node from the end of the list
 * and return its head.
 *
 * @see <a href="https://leetcode.com/problems/remove-nth-node-from-end-of-list/">Remove Nth Node From End of List - LeetCode</a>
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

    public ListNode removeNthFromEnd(ListNode head, int n) {
        if (head == null || n <= 0) return head;

        // dummy precedes head so removing the head itself is not a special case
        ListNode dummy = new ListNode(-1, head),
            prev = dummy,
            slow = head,
            fast = head;

        // push fast n nodes ahead to create a fixed n-node gap to slow
        for (; n > 0; n--) fast = fast.next;

        // walk the gap to the end; slow lands exactly on the target node,
        // prev on the node right before it
        while (fast != null) {
            prev = slow;
            slow = slow.next;
            fast = fast.next;
        }

        // unlink slow (the nth-from-end node)
        prev.next = slow.next;
        slow.next = null;

        return dummy.next;
    }
}
