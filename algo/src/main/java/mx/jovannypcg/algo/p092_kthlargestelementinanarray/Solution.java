package mx.jovannypcg.algo.p092_kthlargestelementinanarray;

import java.util.*;

/**
 * Given an integer array {@code nums} and an integer {@code k}, return the {@code k}th
 * largest element in the array.
 *
 * <p>Note that it is the {@code k}th largest element in the sorted order, not the
 * {@code k}th distinct element.
 *
 * @see <a href="https://leetcode.com/problems/kth-largest-element-in-an-array/">Kth Largest Element in an Array - LeetCode</a>
 */
public class Solution {
    public int findKthLargest(int[] nums, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();

        for (int num : nums) {
            minHeap.offer(num);

            if (minHeap.size() > k) minHeap.poll();
        }

        return minHeap.peek();
    }
}
