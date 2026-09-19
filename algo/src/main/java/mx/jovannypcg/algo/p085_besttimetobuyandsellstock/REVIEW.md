| | |
|---|---|
| **Solved on** | 2026-09-19 |
| **DSA Category** | Two Pointers |

## 1. Your Solution Assessment

**Correctness:** Handles all cases correctly, including the `null`/short-array guard, strictly
decreasing prices, duplicates, and the case where the best pair isn't the global min/max. The
two-pointer formulation is a valid variant of the classic "track the running minimum" pattern:
`left` always points at the cheapest price seen so far, and `right` scans forward looking for a
better sell day.

**Code quality:** Clear and compact. Variable names (`left`, `right`) read naturally for a
two-pointer solution, and the comments now explain *why* each branch is taken. Minor style note:
`_maxProfit` uses a leading underscore only to avoid shadowing the method name — a name like
`bestProfit` would avoid that need entirely.

**Time complexity:** O(n) — each index is visited once as `right` advances every iteration.

**Space complexity:** O(1) — only three integer variables regardless of input size.

**Algorithm trace** (annotated array, `prices = [7, 1, 5, 3, 6, 4]`):

```
left=0 right=1  prices[1]=1 < prices[0]=7 → new low, left moves to 1
[7, 1, 5, 3, 6, 4]
 L  R

left=1 right=2  prices[2]=5 ≥ prices[1]=1 → profit=5-1=4, maxProfit=4
[7, 1, 5, 3, 6, 4]
    L  R

left=1 right=3  prices[3]=3 ≥ prices[1]=1 → profit=3-1=2, maxProfit stays 4
[7, 1, 5, 3, 6, 4]
    L     R

left=1 right=4  prices[4]=6 ≥ prices[1]=1 → profit=6-1=5, maxProfit=5
[7, 1, 5, 3, 6, 4]
    L        R

left=1 right=5  prices[5]=4 ≥ prices[1]=1 → profit=4-1=3, maxProfit stays 5
[7, 1, 5, 3, 6, 4]
    L           R
```
→ return `5`

## 2. Optimal Approach

Track the minimum price seen so far in a single variable while scanning left to right. At each
day, the best possible profit if selling *today* is `prices[i] - minPriceSoFar`. Keep a running
max of that value. This is functionally the same O(n)/O(1) approach as the submitted solution,
just expressed with one running-min variable instead of a second pointer — slightly simpler to
read since there's no branch that "moves" a pointer.

**Time complexity:** O(n) — single pass.
**Space complexity:** O(1) — two variables.

```java
public int maxProfit(int[] prices) {
    int minPrice = Integer.MAX_VALUE;
    int maxProfit = 0;

    for (int price : prices) {
        minPrice = Math.min(minPrice, price);
        maxProfit = Math.max(maxProfit, price - minPrice);
    }

    return maxProfit;
}
```

**Algorithm trace** (step table, `prices = [7, 1, 5, 3, 6, 4]`):

| i | price | minPrice before | price - minPrice | maxProfit |
|---|-------|------------------|-------------------|-----------|
| 0 | 7 | ∞ | -∞ (skipped) | 0 |
| 1 | 1 | 7 → 1 | 0 | 0 |
| 2 | 5 | 1 | 4 | 4 |
| 3 | 3 | 1 | 2 | 4 |
| 4 | 6 | 1 | 5 | 5 |
| 5 | 4 | 1 | 3 | 5 |
→ return `5`

## 3. Alternative Approaches

### Brute force — check every buy/sell pair
For every pair `(i, j)` with `i < j`, compute `prices[j] - prices[i]` and keep the max positive
value.

**Time complexity:** O(n²) — nested loop over all pairs.
**Space complexity:** O(1).
**When acceptable:** Small inputs or as a warm-up answer in an interview before optimizing to
O(n); not viable at the stated constraint of up to 10^5 days.

**Algorithm trace** (call-stack-style step table isn't applicable since it's iterative; using a
step table, `prices = [7, 1, 5, 3, 6, 4]`, showing only profitable pairs found):

| i | j | prices[j]-prices[i] | best so far |
|---|---|----------------------|-------------|
| 0 | 4 | 6-7 = -1 | 0 |
| 1 | 2 | 5-1 = 4 | 4 |
| 1 | 4 | 6-1 = 5 | 5 |
| 3 | 4 | 6-3 = 3 | 5 |
→ return `5`

### Kadane's algorithm on daily price differences
Transform the problem into "maximum subarray sum" on the array of day-to-day differences
(`diff[i] = prices[i] - prices[i-1]`), then run Kadane's algorithm. This works because the best
single buy/sell window's profit equals the max-subarray-sum of the differences within it.

**Time complexity:** O(n) — one pass to build differences conceptually (can be fused into a
single loop), one pass for Kadane's.
**Space complexity:** O(1) if differences are computed on the fly instead of stored in an array.
**When acceptable:** Good to mention as a bridge to Maximum Subarray (LeetCode 53) if the
interviewer probes for a DP-flavored alternative, but offers no advantage over the running-min
approach for this specific problem.

**Algorithm trace** (step table, `prices = [7, 1, 5, 3, 6, 4]`, diffs `= [-6, 4, -2, 3, -2]`):

| i | diff | currentSum = max(diff, currentSum+diff) | maxSum |
|---|------|-------------------------------------------|--------|
| 0 | -6 | -6 | -6 |
| 1 | 4 | max(4, -6+4=-2) = 4 | 4 |
| 2 | -2 | max(-2, 4-2=2) = 2 | 4 |
| 3 | 3 | max(3, 2+3=5) = 5 | 5 |
| 4 | -2 | max(-2, 5-2=3) = 3 | 5 |
→ return `max(5, 0) = 5`
