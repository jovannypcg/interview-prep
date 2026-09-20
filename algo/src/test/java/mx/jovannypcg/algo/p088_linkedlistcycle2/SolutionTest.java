package mx.jovannypcg.algo.p088_linkedlistcycle2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SolutionTest {

    private Solution solution;

    @BeforeEach
    void setUp() {
        solution = new Solution();
    }

    private Solution.ListNode buildList(int[] vals, int pos) {
        if (vals.length == 0) return null;

        Solution.ListNode[] nodes = new Solution.ListNode[vals.length];
        for (int i = 0; i < vals.length; i++) {
            nodes[i] = new Solution.ListNode(vals[i]);
        }
        for (int i = 0; i < vals.length - 1; i++) {
            nodes[i].next = nodes[i + 1];
        }
        if (pos >= 0) {
            nodes[vals.length - 1].next = nodes[pos];
        }

        return nodes[0];
    }

    @Test
    @DisplayName("classic example: tail connects back to a middle node → returns that node")
    void classicExampleWithCycle() {
        Solution.ListNode head = buildList(new int[] {3, 2, 0, -4}, 1);
        Solution.ListNode expectedEntry = head.next;

        assertThat(solution.detectCycle(head)).isSameAs(expectedEntry);
    }

    @Test
    @DisplayName("two nodes, tail connects back to head → returns the head")
    void twoNodesCycleBackToHead() {
        Solution.ListNode head = buildList(new int[] {1, 2}, 0);

        assertThat(solution.detectCycle(head)).isSameAs(head);
    }

    @Test
    @DisplayName("single node, no cycle (minimum non-empty length per constraints) → returns null")
    void singleNodeNoCycle() {
        Solution.ListNode head = buildList(new int[] {1}, -1);

        assertThat(solution.detectCycle(head)).isNull();
    }

    @Test
    @DisplayName("empty list (minimum length per constraints) → returns null")
    void emptyList() {
        Solution.ListNode head = buildList(new int[] {}, -1);

        assertThat(solution.detectCycle(head)).isNull();
    }

    @Test
    @DisplayName("single node pointing to itself → returns that same node")
    void singleNodeSelfLoop() {
        Solution.ListNode head = buildList(new int[] {1}, 0);

        assertThat(solution.detectCycle(head)).isSameAs(head);
    }

    @Test
    @DisplayName("cycle starting mid-list, after a non-cyclic prefix → returns the entry node")
    void cycleStartingMidList() {
        Solution.ListNode head = buildList(new int[] {1, 2, 3, 4, 5, 6}, 2);
        Solution.ListNode expectedEntry = head.next.next;

        assertThat(solution.detectCycle(head)).isSameAs(expectedEntry);
    }

    @Test
    @DisplayName("cycle that loops the entire list back to the head → returns the head")
    void fullLoopBackToHead() {
        Solution.ListNode head = buildList(new int[] {5, 4, 3, 2, 1}, 0);

        assertThat(solution.detectCycle(head)).isSameAs(head);
    }

    @Test
    @DisplayName("two-node list, no cycle → returns null")
    void twoNodesNoCycle() {
        Solution.ListNode head = buildList(new int[] {1, 2}, -1);

        assertThat(solution.detectCycle(head)).isNull();
    }

    @Test
    @DisplayName("duplicate values with no cycle → not fooled by equal node values")
    void duplicateValuesNoCycle() {
        Solution.ListNode head = buildList(new int[] {7, 7, 7, 7}, -1);

        assertThat(solution.detectCycle(head)).isNull();
    }

    @Test
    @DisplayName("duplicate values with a cycle → identifies the entry node by identity, not value")
    void duplicateValuesWithCycle() {
        Solution.ListNode head = buildList(new int[] {7, 7, 7, 7}, 1);
        Solution.ListNode expectedEntry = head.next;

        assertThat(solution.detectCycle(head)).isSameAs(expectedEntry);
    }

    @Test
    @DisplayName("mixed negative and positive values, no cycle")
    void mixedSignValuesNoCycle() {
        Solution.ListNode head = buildList(new int[] {-5, 0, 5, -10}, -1);

        assertThat(solution.detectCycle(head)).isNull();
    }

    @Test
    @DisplayName("node values at min and max boundaries (-100000 and 100000), no cycle")
    void boundaryValuesNoCycle() {
        Solution.ListNode head = buildList(new int[] {-100000, 0, 100000}, -1);

        assertThat(solution.detectCycle(head)).isNull();
    }

    @Test
    @DisplayName("list of 10000 nodes (maximum length per constraints), no cycle")
    void maxLengthListNoCycle() {
        int[] vals = new int[10000];
        for (int i = 0; i < 10000; i++) vals[i] = i;

        Solution.ListNode head = buildList(vals, -1);

        assertThat(solution.detectCycle(head)).isNull();
    }

    @Test
    @DisplayName("list of 10000 nodes (maximum length per constraints), cycle back to head")
    void maxLengthListWithCycle() {
        int[] vals = new int[10000];
        for (int i = 0; i < 10000; i++) vals[i] = i;

        Solution.ListNode head = buildList(vals, 0);

        assertThat(solution.detectCycle(head)).isSameAs(head);
    }
}
