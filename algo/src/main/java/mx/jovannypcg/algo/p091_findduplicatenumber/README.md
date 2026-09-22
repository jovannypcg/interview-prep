# Find the Duplicate Number

**Date added:** 2026-09-21

## Problem Description

Given an array of integers `nums` containing `n + 1` integers where each integer is in the
range `[1, n]` inclusive, there is only one repeated number in `nums`, and it may appear
two or more times. Return this repeated number.

You must solve the problem without modifying the array `nums` and using only constant extra
space.

**Source:** https://leetcode.com/problems/find-the-duplicate-number/

## Examples

**Example 1**
```
Input: nums = [1,3,4,2,2]
Output: 2
Explanation: 2 is the only number that appears more than once (at indices 3 and 4).
```

**Example 2**
```
Input: nums = [3,1,3,4,2]
Output: 3
Explanation: 3 is the only number that appears more than once (at indices 0 and 2).
```

**Example 3**
```
Input: nums = [3,3,3,3,3]
Output: 3
Explanation: Every element is 3, so it is trivially the repeated number.
```

**Example 4**
```
Input: nums = [1,1]
Output: 1
Explanation: Minimal valid input with n = 1: nums has length n + 1 = 2, and the only possible
value in range [1, 1] is 1, repeated twice.
```

**Example 5**
```
Input: nums = [2,2,2,2,2,1]
Output: 2
Explanation: n = 5, and the duplicate (2) appears far more times than the other value (1),
which appears zero times — the array doesn't need to contain every value in [1, n].
```

**Example 6**
```
Input: nums = [1,2,3,4,4]
Output: 4
Explanation: The duplicate is the maximum possible value in range [1, n], appearing at the
last two indices.
```

**Example 7**
```
Input: nums = [5,4,3,2,1,3]
Output: 3
Explanation: A larger, unsorted array where the duplicate (3) is not adjacent to its other
occurrence, forcing an approach that doesn't rely on scanning nearby elements.
```

## Constraints

- `1 <= n <= 10^5`
- `nums.length == n + 1`
- `1 <= nums[i] <= n`
- All the integers in `nums` appear only once except for precisely one integer which appears
  two or more times.

## Hints

1. Since every value is in `[1, n]` and there are `n + 1` values, think about what a value
   in `nums` can represent besides just a number — what if you treated it as a pointer to
   another index?
2. If you built a graph where each index `i` points to `nums[i]`, what property would that
   graph have given there's a duplicate?
3. A duplicate value means two indices point to the same next index — what shape does that
   create in the graph (think about linked lists)?
4. This is structurally identical to detecting a cycle in a linked list — which algorithm
   solves that in O(1) space?
5. Use two pointers moving at different speeds (slow and fast) to find the cycle, then use a
   second phase to find the exact entry point of the cycle — that entry point is your answer.
