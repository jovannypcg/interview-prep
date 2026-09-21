package mx.jovannypcg.algo.p090_addtwonumbers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SolutionTest {

    private Solution solution;

    @BeforeEach
    void setUp() {
        solution = new Solution();
    }

    private Solution.ListNode buildList(int... vals) {
        Solution.ListNode dummy = new Solution.ListNode(0);
        Solution.ListNode curr = dummy;
        for (int v : vals) {
            curr.next = new Solution.ListNode(v);
            curr = curr.next;
        }
        return dummy.next;
    }

    private int[] toArray(Solution.ListNode node) {
        List<Integer> list = new ArrayList<>();
        while (node != null) {
            list.add(node.val);
            node = node.next;
        }
        return list.stream().mapToInt(Integer::intValue).toArray();
    }

    @Test
    @DisplayName("single-digit lists with no carry → matches example semantics of simple addition")
    void singleDigitNoCarry() {
        Solution.ListNode l1 = buildList(2);
        Solution.ListNode l2 = buildList(3);
        Solution.ListNode result = solution.addTwoNumbers(l1, l2);
        assertThat(toArray(result)).containsExactly(5);
    }

    @Test
    @DisplayName("both lists are [0] → returns [0], matches example 2")
    void bothZero() {
        Solution.ListNode l1 = buildList(0);
        Solution.ListNode l2 = buildList(0);
        Solution.ListNode result = solution.addTwoNumbers(l1, l2);
        assertThat(toArray(result)).containsExactly(0);
    }

    @Test
    @DisplayName("classic three-digit addition → matches example 1 (342 + 465 = 807)")
    void classicThreeDigitAddition() {
        Solution.ListNode l1 = buildList(2, 4, 3);
        Solution.ListNode l2 = buildList(5, 6, 4);
        Solution.ListNode result = solution.addTwoNumbers(l1, l2);
        assertThat(toArray(result)).containsExactly(7, 0, 8);
    }

    @Test
    @DisplayName("single-digit addition with carry (5 + 5 = 10) → produces extra node")
    void singleDigitWithCarryProducesExtraNode() {
        Solution.ListNode l1 = buildList(5);
        Solution.ListNode l2 = buildList(5);
        Solution.ListNode result = solution.addTwoNumbers(l1, l2);
        assertThat(toArray(result)).containsExactly(0, 1);
    }

    @Test
    @DisplayName("l2 shorter than l1 → remaining l1 digits carried through, matches example 5")
    void l2ShorterThanL1() {
        Solution.ListNode l1 = buildList(1, 8);
        Solution.ListNode l2 = buildList(0);
        Solution.ListNode result = solution.addTwoNumbers(l1, l2);
        assertThat(toArray(result)).containsExactly(1, 8);
    }

    @Test
    @DisplayName("l1 shorter than l2 with trailing carry → matches example 6 (942 + 9465 = 10407)")
    void l1ShorterThanL2WithTrailingCarry() {
        Solution.ListNode l1 = buildList(2, 4, 9);
        Solution.ListNode l2 = buildList(5, 6, 4, 9);
        Solution.ListNode result = solution.addTwoNumbers(l1, l2);
        assertThat(toArray(result)).containsExactly(7, 0, 4, 0, 1);
    }

    @Test
    @DisplayName("multiple consecutive carries ripple through → matches example 7 (99 + 1 = 100)")
    void multipleConsecutiveCarriesRipple() {
        Solution.ListNode l1 = buildList(9, 9);
        Solution.ListNode l2 = buildList(1);
        Solution.ListNode result = solution.addTwoNumbers(l1, l2);
        assertThat(toArray(result)).containsExactly(0, 0, 1);
    }

    @Test
    @DisplayName("both lists all nines with different lengths → matches example 3")
    void bothListsAllNinesDifferentLengths() {
        Solution.ListNode l1 = buildList(9, 9, 9, 9, 9, 9, 9);
        Solution.ListNode l2 = buildList(9, 9, 9, 9);
        Solution.ListNode result = solution.addTwoNumbers(l1, l2);
        assertThat(toArray(result)).containsExactly(8, 9, 9, 9, 0, 0, 0, 1);
    }

    @Test
    @DisplayName("node values at maximum digit boundary (9) with no carry → 9 + 0 = 9")
    void maxDigitBoundaryNoCarry() {
        Solution.ListNode l1 = buildList(9);
        Solution.ListNode l2 = buildList(0);
        Solution.ListNode result = solution.addTwoNumbers(l1, l2);
        assertThat(toArray(result)).containsExactly(9);
    }

    @Test
    @DisplayName("both lists at maximum length (100 nodes of 9s) → correct carry propagation across full length")
    void maxLengthListsBothAllNines() {
        int[] nines = new int[100];
        for (int i = 0; i < 100; i++) nines[i] = 9;
        Solution.ListNode l1 = buildList(nines);
        Solution.ListNode l2 = buildList(nines);

        Solution.ListNode result = solution.addTwoNumbers(l1, l2);

        int[] expected = new int[101];
        expected[0] = 8;
        for (int i = 1; i < 100; i++) expected[i] = 9;
        expected[100] = 1;
        assertThat(toArray(result)).containsExactly(expected);
    }
}
