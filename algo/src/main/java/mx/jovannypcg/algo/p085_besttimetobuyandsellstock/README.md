# Best Time to Buy and Sell Stocks

**Date added:** 2026-09-19

## Problem Description

You are given an array `prices` where `prices[i]` is the price of a given stock on the `i`th
day.

You want to maximize your profit by choosing a single day to buy one stock and choosing a
different day in the future to sell that stock.

Return the maximum profit you can achieve from this transaction. If you cannot achieve any
profit, return `0`.

**Source:** https://leetcode.com/problems/best-time-to-buy-and-sell-stock/

## Examples

**Example 1**
```
Input: prices = [7,1,5,3,6,4]
Output: 5
Explanation: Buy on day 2 (price = 1) and sell on day 5 (price = 6), profit = 6-1 = 5.
Note that buying on day 2 and selling on day 1 is not allowed because you must buy before you sell.
```

**Example 2**
```
Input: prices = [7,6,4,3,1]
Output: 0
Explanation: In this case, no transactions are done and the max profit = 0.
```

**Example 3**
```
Input: prices = [1,2]
Output: 1
Explanation: Buy on day 1 (price = 1) and sell on day 2 (price = 2), profit = 2-1 = 1.
```

**Example 4**
```
Input: prices = [2,4,1]
Output: 2
Explanation: Buy on day 1 (price = 2) and sell on day 2 (price = 4), profit = 4-2 = 2.
Selling after day 3 (price = 1) would result in a loss, so it is not considered.
```

**Example 5**
```
Input: prices = [3,3,3,3]
Output: 0
Explanation: The price never changes, so no transaction can yield a profit.
```

**Example 6**
```
Input: prices = [1,2,3,4,5]
Output: 4
Explanation: Buy on day 1 (price = 1) and sell on day 5 (price = 5), profit = 5-1 = 4.
Prices increase every day, so the best strategy is to buy on the very first day and sell on the very last.
```

**Example 7**
```
Input: prices = [0]
Output: 0
Explanation: With only one day available there is no future day to sell on, so no transaction can be made.
```

## Constraints

- `1 <= prices.length <= 10^5`
- `0 <= prices[i] <= 10^4`

## Hints

1. A brute-force approach would check every pair of buy/sell days — what is its time complexity?
2. Can you avoid re-scanning the array for every possible buy day?
3. As you scan left to right, think about the lowest price you've seen *so far*.
4. At each day, the best possible profit if you sold today depends only on that running minimum.
5. Track the running minimum price and the best profit seen so far in a single pass through the array.
