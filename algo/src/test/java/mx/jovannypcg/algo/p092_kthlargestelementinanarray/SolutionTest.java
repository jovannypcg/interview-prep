package mx.jovannypcg.algo.p092_kthlargestelementinanarray;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SolutionTest {

    @Test
    @DisplayName("example from problem statement: k=2 returns 5")
    void example1() {
        Solution solution = new Solution();
        int[] nums = {3, 2, 1, 5, 6, 4};

        int result = solution.findKthLargest(nums, 2);

        assertThat(result).isEqualTo(5);
    }

    @Test
    @DisplayName("example with duplicates: k=4 returns 4")
    void example2() {
        Solution solution = new Solution();
        int[] nums = {3, 2, 3, 1, 2, 4, 5, 5, 6};

        int result = solution.findKthLargest(nums, 4);

        assertThat(result).isEqualTo(4);
    }

    @Test
    @DisplayName("single element array with k=1 returns that element")
    void singleElement() {
        Solution solution = new Solution();
        int[] nums = {1};

        int result = solution.findKthLargest(nums, 1);

        assertThat(result).isEqualTo(1);
    }

    @Test
    @DisplayName("k equals array length returns the smallest element")
    void kEqualsLength() {
        Solution solution = new Solution();
        int[] nums = {2, 1};

        int result = solution.findKthLargest(nums, 2);

        assertThat(result).isEqualTo(1);
    }

    @Test
    @DisplayName("k=1 returns the maximum element")
    void kEqualsOneReturnsMax() {
        Solution solution = new Solution();
        int[] nums = {9, 3, 2, 4, 8, 1, 7, 6, 5};

        int result = solution.findKthLargest(nums, 1);

        assertThat(result).isEqualTo(9);
    }

    @Test
    @DisplayName("all duplicate elements returns the repeated value regardless of k")
    void allDuplicates() {
        Solution solution = new Solution();
        int[] nums = {7, 7, 7, 7};

        int result = solution.findKthLargest(nums, 3);

        assertThat(result).isEqualTo(7);
    }

    @Test
    @DisplayName("negative numbers only: k=2 returns second largest negative value")
    void negativeNumbers() {
        Solution solution = new Solution();
        int[] nums = {-1, -2, -3, -4};

        int result = solution.findKthLargest(nums, 2);

        assertThat(result).isEqualTo(-2);
    }

    @Test
    @DisplayName("mix of negative and positive numbers")
    void mixedSignNumbers() {
        Solution solution = new Solution();
        int[] nums = {-10, 4, -1, 0, 3, -5};

        int result = solution.findKthLargest(nums, 3);

        assertThat(result).isEqualTo(0);
    }

    @Test
    @DisplayName("boundary values from constraints: nums[i] = -10^4 and 10^4")
    void boundaryValueRange() {
        Solution solution = new Solution();
        int[] nums = {10000, -10000, 0};

        int result = solution.findKthLargest(nums, 1);

        assertThat(result).isEqualTo(10000);
    }

    @Test
    @DisplayName("largest k retrieves the boundary minimum value")
    void boundaryValueRangeSmallest() {
        Solution solution = new Solution();
        int[] nums = {10000, -10000, 0};

        int result = solution.findKthLargest(nums, 3);

        assertThat(result).isEqualTo(-10000);
    }

    @Test
    @DisplayName("large input near upper constraint bound of 10^5 elements")
    void largeInput() {
        Solution solution = new Solution();
        int size = 100_000;
        int[] nums = new int[size];
        for (int i = 0; i < size; i++) {
            nums[i] = i;
        }

        int result = solution.findKthLargest(nums, 1);

        assertThat(result).isEqualTo(size - 1);
    }

    @Test
    @DisplayName("already sorted descending array")
    void sortedDescending() {
        Solution solution = new Solution();
        int[] nums = {6, 5, 4, 3, 2, 1};

        int result = solution.findKthLargest(nums, 5);

        assertThat(result).isEqualTo(2);
    }

    @Test
    @DisplayName("already sorted ascending array")
    void sortedAscending() {
        Solution solution = new Solution();
        int[] nums = {1, 2, 3, 4, 5, 6};

        int result = solution.findKthLargest(nums, 3);

        assertThat(result).isEqualTo(4);
    }
}
