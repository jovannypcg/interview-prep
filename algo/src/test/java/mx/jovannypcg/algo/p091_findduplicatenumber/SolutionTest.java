package mx.jovannypcg.algo.p091_findduplicatenumber;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SolutionTest {

    @Test
    @DisplayName("typical case, duplicate appears at the end")
    void typicalCaseDuplicateAtEnd() {
        Solution solution = new Solution();

        int result = solution.findDuplicate(new int[]{1, 3, 4, 2, 2});

        assertThat(result).isEqualTo(2);
    }

    @Test
    @DisplayName("typical case, duplicate appears near the start")
    void typicalCaseDuplicateNearStart() {
        Solution solution = new Solution();

        int result = solution.findDuplicate(new int[]{3, 1, 3, 4, 2});

        assertThat(result).isEqualTo(3);
    }

    @Test
    @DisplayName("every element is the same value")
    void everyElementIsTheSameValue() {
        Solution solution = new Solution();

        int result = solution.findDuplicate(new int[]{3, 3, 3, 3, 3});

        assertThat(result).isEqualTo(3);
    }

    @Test
    @DisplayName("minimal input, n = 1")
    void minimalInputNEqualsOne() {
        Solution solution = new Solution();

        int result = solution.findDuplicate(new int[]{1, 1});

        assertThat(result).isEqualTo(1);
    }

    @Test
    @DisplayName("duplicate is the minimum possible value in range [1, n]")
    void duplicateIsMinimumValue() {
        Solution solution = new Solution();

        int result = solution.findDuplicate(new int[]{2, 2, 2, 2, 2, 1});

        assertThat(result).isEqualTo(2);
    }

    @Test
    @DisplayName("duplicate is the maximum possible value in range [1, n]")
    void duplicateIsMaximumValue() {
        Solution solution = new Solution();

        int result = solution.findDuplicate(new int[]{1, 2, 3, 4, 4});

        assertThat(result).isEqualTo(4);
    }

    @Test
    @DisplayName("larger unsorted array with duplicate not adjacent to its other occurrence")
    void largerUnsortedArrayDuplicateNotAdjacent() {
        Solution solution = new Solution();

        int result = solution.findDuplicate(new int[]{5, 4, 3, 2, 1, 3});

        assertThat(result).isEqualTo(3);
    }

    @Test
    @DisplayName("duplicate value repeated three or more times")
    void duplicateRepeatedThreeOrMoreTimes() {
        Solution solution = new Solution();

        int result = solution.findDuplicate(new int[]{1, 4, 4, 2, 4});

        assertThat(result).isEqualTo(4);
    }

    @Test
    @DisplayName("array does not modify the input nums")
    void doesNotModifyInputArray() {
        Solution solution = new Solution();
        int[] nums = {1, 3, 4, 2, 2};
        int[] original = nums.clone();

        solution.findDuplicate(nums);

        assertThat(nums).isEqualTo(original);
    }

    @Test
    @DisplayName("large n near the upper bound of the constraint")
    void largeNNearUpperBound() {
        int n = 100_000;
        int[] nums = new int[n + 1];

        for (int i = 0; i < n; i++) {
            nums[i] = i + 1;
        }
        nums[n] = 1;

        Solution solution = new Solution();

        int result = solution.findDuplicate(nums);

        assertThat(result).isEqualTo(1);
    }
}
