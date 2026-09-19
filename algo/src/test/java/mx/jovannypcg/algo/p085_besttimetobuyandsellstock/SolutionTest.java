package mx.jovannypcg.algo.p085_besttimetobuyandsellstock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SolutionTest {
    private final Solution solution = new Solution();

    @Test
    @DisplayName("classic dip-then-rise → max profit from lowest valley to highest peak after it")
    void classicDipThenRise() {
        int[] prices = {7, 1, 5, 3, 6, 4};

        assertThat(solution.maxProfit(prices)).isEqualTo(5);
    }

    @Test
    @DisplayName("strictly decreasing prices → no profit possible, returns 0")
    void strictlyDecreasingPrices() {
        int[] prices = {7, 6, 4, 3, 1};

        assertThat(solution.maxProfit(prices)).isEqualTo(0);
    }

    @Test
    @DisplayName("two days, price goes up → profit is the difference")
    void twoDaysIncreasing() {
        int[] prices = {1, 2};

        assertThat(solution.maxProfit(prices)).isEqualTo(1);
    }

    @Test
    @DisplayName("two days, price goes down → returns 0")
    void twoDaysDecreasing() {
        int[] prices = {5, 2};

        assertThat(solution.maxProfit(prices)).isEqualTo(0);
    }

    @Test
    @DisplayName("single element array → no future day to sell, returns 0")
    void singleElementArray() {
        int[] prices = {5};

        assertThat(solution.maxProfit(prices)).isEqualTo(0);
    }

    @Test
    @DisplayName("single element at minimum boundary price 0 → returns 0")
    void singleElementMinimumPrice() {
        int[] prices = {0};

        assertThat(solution.maxProfit(prices)).isEqualTo(0);
    }

    @Test
    @DisplayName("all prices identical → no profit possible, returns 0")
    void allDuplicatePrices() {
        int[] prices = {3, 3, 3, 3};

        assertThat(solution.maxProfit(prices)).isEqualTo(0);
    }

    @Test
    @DisplayName("strictly increasing prices → buy on first day, sell on last day")
    void strictlyIncreasingPrices() {
        int[] prices = {1, 2, 3, 4, 5};

        assertThat(solution.maxProfit(prices)).isEqualTo(4);
    }

    @Test
    @DisplayName("valley in the middle after a peak → ignore the earlier peak, profit from later valley")
    void peakBeforeValley() {
        int[] prices = {2, 4, 1};

        assertThat(solution.maxProfit(prices)).isEqualTo(2);
    }

    @Test
    @DisplayName("best buy/sell pair is not the global min/max in array order")
    void bestPairNotGlobalExtremes() {
        int[] prices = {3, 8, 1, 9};

        assertThat(solution.maxProfit(prices)).isEqualTo(8);
    }

    @Test
    @DisplayName("price touches the maximum boundary value 10^4 → profit computed correctly")
    void maximumBoundaryPrice() {
        int[] prices = {0, 10_000};

        assertThat(solution.maxProfit(prices)).isEqualTo(10_000);
    }

    @Test
    @DisplayName("large input at the upper length boundary (10^5 days), monotonically increasing")
    void largeInputAtLengthBoundary() {
        int n = 100_000;
        int[] prices = new int[n];

        for (int i = 0; i < n; i++) {
            prices[i] = i;
        }

        assertThat(solution.maxProfit(prices)).isEqualTo(n - 1);
    }

    @Test
    @DisplayName("zero price appears mid-array as the true valley → profit measured from it")
    void zeroPriceValleyMidArray() {
        int[] prices = {5, 4, 0, 3, 1, 6};

        assertThat(solution.maxProfit(prices)).isEqualTo(6);
    }
}
