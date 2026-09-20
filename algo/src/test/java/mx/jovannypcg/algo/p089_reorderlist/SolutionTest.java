package mx.jovannypcg.algo.p089_reorderlist;

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

    private Solution.ListNode buildList(int[] vals) {
        if (vals.length == 0) return null;

        Solution.ListNode head = new Solution.ListNode(vals[0]);
        Solution.ListNode current = head;

        for (int i = 1; i < vals.length; i++) {
            current.next = new Solution.ListNode(vals[i]);
            current = current.next;
        }

        return head;
    }

    private List<Integer> toValues(Solution.ListNode head, int expectedLength) {
        List<Integer> values = new ArrayList<>();
        Solution.ListNode current = head;
        int guard = 0;

        while (current != null) {
            values.add(current.val);
            current = current.next;
            guard++;

            if (guard > expectedLength) {
                throw new IllegalStateException("Traversal exceeded expected length — list likely contains a cycle");
            }
        }

        return values;
    }

    private int[] expectedOrder(int[] vals) {
        int[] result = new int[vals.length];
        int i = 0;
        int j = vals.length - 1;
        int idx = 0;
        boolean front = true;

        while (i <= j) {
            if (front) {
                result[idx++] = vals[i++];
            } else {
                result[idx++] = vals[j--];
            }
            front = !front;
        }

        return result;
    }

    @Test
    @DisplayName("even-length classic example [1,2,3,4] → [1,4,2,3]")
    void evenLengthClassicExample() {
        int[] vals = {1, 2, 3, 4};
        Solution.ListNode head = buildList(vals);

        solution.reorderList(head);

        assertThat(toValues(head, vals.length)).containsExactly(1, 4, 2, 3);
    }

    @Test
    @DisplayName("odd-length classic example [1,2,3,4,5] → [1,5,2,4,3]")
    void oddLengthClassicExample() {
        int[] vals = {1, 2, 3, 4, 5};
        Solution.ListNode head = buildList(vals);

        solution.reorderList(head);

        assertThat(toValues(head, vals.length)).containsExactly(1, 5, 2, 4, 3);
    }

    @Test
    @DisplayName("single node (minimum length per constraints) → unchanged")
    void singleNode() {
        int[] vals = {1};
        Solution.ListNode head = buildList(vals);

        solution.reorderList(head);

        assertThat(toValues(head, vals.length)).containsExactly(1);
    }

    @Test
    @DisplayName("two nodes → unchanged, since L0 -> Ln is already the original order")
    void twoNodes() {
        int[] vals = {1, 2};
        Solution.ListNode head = buildList(vals);

        solution.reorderList(head);

        assertThat(toValues(head, vals.length)).containsExactly(1, 2);
    }

    @Test
    @DisplayName("three nodes [1,2,3] → [1,3,2]")
    void threeNodes() {
        int[] vals = {1, 2, 3};
        Solution.ListNode head = buildList(vals);

        solution.reorderList(head);

        assertThat(toValues(head, vals.length)).containsExactly(1, 3, 2);
    }

    @Test
    @DisplayName("duplicate values [7,7,7,7,7,7] → reordered node identities, same values")
    void duplicateValues() {
        int[] vals = {7, 7, 7, 7, 7, 7};
        Solution.ListNode head = buildList(vals);

        solution.reorderList(head);

        assertThat(toValues(head, vals.length)).containsExactly(7, 7, 7, 7, 7, 7);
    }

    @Test
    @DisplayName("boundary values (min 1, max 1000) [1,1000,1,1000,1] → [1,1,1000,1000,1]")
    void boundaryValues() {
        int[] vals = {1, 1000, 1, 1000, 1};
        Solution.ListNode head = buildList(vals);

        solution.reorderList(head);

        assertThat(toValues(head, vals.length)).containsExactly(1, 1, 1000, 1000, 1);
    }

    @Test
    @DisplayName("all nodes at the minimum value (1) → order among equal values is still correct")
    void allNodesAtMinValue() {
        int[] vals = {1, 1, 1, 1};
        Solution.ListNode head = buildList(vals);

        solution.reorderList(head);

        assertThat(toValues(head, vals.length)).containsExactly(1, 1, 1, 1);
    }

    @Test
    @DisplayName("all nodes at the maximum value (1000) → order among equal values is still correct")
    void allNodesAtMaxValue() {
        int[] vals = {1000, 1000, 1000, 1000, 1000};
        Solution.ListNode head = buildList(vals);

        solution.reorderList(head);

        assertThat(toValues(head, vals.length)).containsExactly(1000, 1000, 1000, 1000, 1000);
    }

    @Test
    @DisplayName("four distinct values [10,20,30,40] → [10,40,20,30]")
    void fourDistinctValues() {
        int[] vals = {10, 20, 30, 40};
        Solution.ListNode head = buildList(vals);

        solution.reorderList(head);

        assertThat(toValues(head, vals.length)).containsExactly(10, 40, 20, 30);
    }

    @Test
    @DisplayName("reordered list has no cycle: tail's next pointer is null")
    void reorderedListTerminatesWithNull() {
        int[] vals = {1, 2, 3, 4, 5, 6};
        Solution.ListNode head = buildList(vals);

        solution.reorderList(head);

        Solution.ListNode current = head;
        for (int i = 0; i < vals.length - 1; i++) {
            current = current.next;
        }

        assertThat(current.next).isNull();
    }

    @Test
    @DisplayName("large list (50,000 nodes, maximum length per constraints) reorders correctly")
    void maxLengthList() {
        int n = 50_000;
        int[] vals = new int[n];
        for (int i = 0; i < n; i++) vals[i] = (i % 1000) + 1;

        Solution.ListNode head = buildList(vals);

        solution.reorderList(head);

        int[] expected = expectedOrder(vals);
        assertThat(toValues(head, n)).containsExactly(box(expected));
    }

    private Integer[] box(int[] arr) {
        Integer[] boxed = new Integer[arr.length];
        for (int i = 0; i < arr.length; i++) boxed[i] = arr[i];
        return boxed;
    }
}
