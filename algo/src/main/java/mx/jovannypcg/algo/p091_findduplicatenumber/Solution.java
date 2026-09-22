package mx.jovannypcg.algo.p091_findduplicatenumber;

/**
 * Given an array of integers {@code nums} containing {@code n + 1} integers where each integer
 * is in the range {@code [1, n]} inclusive, there is only one repeated number in {@code nums}.
 * Return this repeated number.
 *
 * <p>You must solve the problem without modifying the array {@code nums} and using only
 * constant extra space.
 *
 * @see <a href="https://leetcode.com/problems/find-the-duplicate-number/">Find the Duplicate Number - LeetCode</a>
 */
public class Solution {
    public int findDuplicate(int[] nums) {
        if (nums == null || nums.length < 2) return -1;

        // Phase 1: treat nums[i] as a pointer to index nums[i] (an implicit linked list).
        // The duplicate value creates a cycle. Advance slow by 1 step, fast by 2 steps,
        // pre-stepped once so the loop condition isn't trivially true at index 0.
        int slow = nums[0];
        int fast = nums[nums[0]];

        // Keep stepping until slow and fast meet somewhere inside the cycle.
        while (slow != fast) {
            slow = nums[slow];
            fast = nums[nums[fast]];
        }

        // Phase 2: reset slow to the head (index 0, guaranteed outside the cycle).
        // Move both pointers 1 step at a time; where they meet is the cycle entrance,
        // which is exactly the duplicate value.
        slow = 0;
        while (slow != fast) {
            slow = nums[slow];
            fast = nums[fast];
        }

        return slow;
    }
}
