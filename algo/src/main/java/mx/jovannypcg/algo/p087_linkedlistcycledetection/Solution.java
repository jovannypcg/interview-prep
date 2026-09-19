package mx.jovannypcg.algo.p087_linkedlistcycledetection;

/**
 * Given the beginning of a linked list {@code head}, return {@code true} if there is a cycle
 * in the linked list. Otherwise, return {@code false}.
 *
 * <p>There is a cycle in a linked list if at least one node in the list can be visited again
 * by following the {@code next} pointer.
 *
 * <p>Internally, {@code index} determines the index of the beginning of the cycle, if it
 * exists. The tail node of the list will set its {@code next} pointer to the {@code index}-th
 * node. If {@code index = -1}, then the tail node points to {@code null} and no cycle exists.
 * Note: {@code index} is not given to you as a parameter.
 *
 * @see <a href="https://neetcode.io/problems/linked-list-cycle-detection">Linked List Cycle Detection - NeetCode</a>
 */
public class Solution {

    public static class ListNode {
        int val;
        ListNode next;

        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    public boolean hasCycle(ListNode head) {
        if (head == null) return false;

        ListNode slow = head;
        ListNode fast = head.next;

        while (fast != null && fast.next != null) {
            if (slow == fast) return true;

            slow = slow.next;
            fast = fast.next.next;
        }

        return false;
    }
}
