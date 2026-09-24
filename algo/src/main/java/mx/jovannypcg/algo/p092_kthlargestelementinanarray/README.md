# Kth Largest Element in an Array

**Date added:** 2026-09-24

## Problem Description

Given an integer array `nums` and an integer `k`, return the `k`th largest element in the
array.

Note that it is the `k`th largest element in the sorted order, not the `k`th distinct
element.

Can you solve it without sorting?

**Source:** https://leetcode.com/problems/kth-largest-element-in-an-array/

## Examples

**Example 1**
```
Input: nums = [3,2,1,5,6,4], k = 2
Output: 5
Explanation: Sorted descending, nums = [6,5,4,3,2,1]. The 2nd largest element is 5.
```

**Example 2**
```
Input: nums = [3,2,3,1,2,4,5,5,6], k = 4
Output: 4
Explanation: Sorted descending, nums = [6,5,5,4,3,3,2,2,1]. The 4th largest element is 4.
```

**Example 3**
```
Input: nums = [1], k = 1
Output: 1
Explanation: With a single element, the 1st largest element is that element itself.
```

**Example 4**
```
Input: nums = [2,1], k = 2
Output: 1
Explanation: Sorted descending, nums = [2,1]. The 2nd largest element is 1 (the smallest).
```

**Example 5**
```
Input: nums = [7,7,7,7], k = 3
Output: 7
Explanation: Duplicates count individually in the sorted order, so every position holds 7.
```

**Example 6**
```
Input: nums = [-1,-2,-3,-4], k = 2
Output: -2
Explanation: Sorted descending, nums = [-1,-2,-3,-4]. The 2nd largest element is -2.
```

**Example 7**
```
Input: nums = [9,3,2,4,8,1,7,6,5], k = 9
Output: 1
Explanation: Sorted descending, nums = [9,8,7,6,5,4,3,2,1]. The 9th largest (i.e. the
smallest) element is 1.
```

## Constraints

- `1 <= k <= nums.length <= 10^5`
- `-10^4 <= nums[i] <= 10^4`

## Hints

1. Sorting the array and picking an index would work — but the problem asks if you can avoid it. What's the time complexity of sorting, and can we do better?
2. Think about what data structure keeps track of the "largest so far" efficiently as you process elements one at a time.
3. A heap (priority queue) can maintain a running view of the top elements without fully sorting the array.
4. Consider bounding the heap's size to exactly `k` — what does the smallest element in such a heap represent?
5. Alternatively, look into Quickselect: a partition-based approach related to quicksort that can find the kth largest in average O(n) time without fully sorting.
