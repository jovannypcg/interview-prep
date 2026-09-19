package mx.jovannypcg.algo.p085_besttimetobuyandsellstock;

/**
 * You are given an array {@code prices} where {@code prices[i]} is the price of a given stock
 * on the {@code i}th day.
 *
 * <p>You want to maximize your profit by choosing a single day to buy one stock and choosing a
 * different day in the future to sell that stock.
 *
 * <p>Return the maximum profit you can achieve from this transaction. If you cannot achieve any
 * profit, return {@code 0}.
 *
 * @see <a href="https://leetcode.com/problems/best-time-to-buy-and-sell-stock/">Problem Source</a>
 */
public class Solution {
    public int maxProfit(int[] prices) {
        // Need at least 2 days to buy and sell
        if (prices == null || prices.length < 2) {
            return 0;
        }

        int left = 0,       // index of the cheapest price seen so far (buy day)
            right = 1,      // index being evaluated as a sell day
            _maxProfit = 0;

        while (right < prices.length) {
            if (prices[right] < prices[left]) {
                // New low found, it becomes the new buy candidate
                left = right;
                right++;
            } else {
                // Selling today beats buying at `left`; keep the best profit
                _maxProfit = Math.max(_maxProfit, prices[right] - prices[left]);
                right++;
            }
        }

        return _maxProfit;
    }
}
