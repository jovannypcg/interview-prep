package mx.jovannypcg.algo.p087_linkedlistcycledetection;

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

    private Solution.ListNode buildList(int[] vals, int index) {
        if (vals.length == 0) return null;

        Solution.ListNode[] nodes = new Solution.ListNode[vals.length];
        for (int i = 0; i < vals.length; i++) {
            nodes[i] = new Solution.ListNode(vals[i]);
        }
        for (int i = 0; i < vals.length - 1; i++) {
            nodes[i].next = nodes[i + 1];
        }
        if (index >= 0) {
            nodes[vals.length - 1].next = nodes[index];
        }

        return nodes[0];
    }

    @Test
    @DisplayName("classic example: tail connects back to a middle node → cycle detected")
    void classicExampleWithCycle() {
        Solution.ListNode head = buildList(new int[] {3, 2, 0, -4}, 1);
        assertThat(solution.hasCycle(head)).isTrue();
    }

    @Test
    @DisplayName("two nodes, tail points to null → no cycle")
    void twoNodesNoCycle() {
        Solution.ListNode head = buildList(new int[] {1, 2}, -1);
        assertThat(solution.hasCycle(head)).isFalse();
    }

    @Test
    @DisplayName("single node, no cycle (minimum non-empty length per constraints)")
    void singleNodeNoCycle() {
        Solution.ListNode head = buildList(new int[] {1}, -1);
        assertThat(solution.hasCycle(head)).isFalse();
    }

    @Test
    @DisplayName("single node pointing to itself → self-loop is a cycle")
    void singleNodeSelfLoop() {
        Solution.ListNode head = buildList(new int[] {1}, 0);
        assertThat(solution.hasCycle(head)).isTrue();
    }

    @Test
    @DisplayName("empty list (minimum length per constraints) → no cycle")
    void emptyList() {
        Solution.ListNode head = buildList(new int[] {}, -1);
        assertThat(solution.hasCycle(head)).isFalse();
    }

    @Test
    @DisplayName("cycle starting mid-list, after a non-cyclic prefix → cycle detected")
    void cycleStartingMidList() {
        Solution.ListNode head = buildList(new int[] {1, 2, 3, 4, 5, 6}, 2);
        assertThat(solution.hasCycle(head)).isTrue();
    }

    @Test
    @DisplayName("cycle that loops the entire list back to the head → cycle detected")
    void fullLoopBackToHead() {
        Solution.ListNode head = buildList(new int[] {5, 4, 3, 2, 1}, 0);
        assertThat(solution.hasCycle(head)).isTrue();
    }

    @Test
    @DisplayName("duplicate values with no cycle → not fooled by equal node values")
    void duplicateValuesNoCycle() {
        Solution.ListNode head = buildList(new int[] {7, 7, 7, 7}, -1);
        assertThat(solution.hasCycle(head)).isFalse();
    }

    @Test
    @DisplayName("duplicate values with a cycle → detected by node identity, not value")
    void duplicateValuesWithCycle() {
        Solution.ListNode head = buildList(new int[] {7, 7, 7, 7}, 1);
        assertThat(solution.hasCycle(head)).isTrue();
    }

    @Test
    @DisplayName("mixed negative and positive values, no cycle")
    void mixedSignValuesNoCycle() {
        Solution.ListNode head = buildList(new int[] {-5, 0, 5, -10}, -1);
        assertThat(solution.hasCycle(head)).isFalse();
    }

    @Test
    @DisplayName("node values at min and max boundaries (-1000 and 1000), no cycle")
    void boundaryValuesNoCycle() {
        Solution.ListNode head = buildList(new int[] {-1000, 0, 1000}, -1);
        assertThat(solution.hasCycle(head)).isFalse();
    }

    @Test
    @DisplayName("list of 1000 nodes (maximum length per constraints), no cycle")
    void maxLengthListNoCycle() {
        int[] vals = new int[1000];
        for (int i = 0; i < 1000; i++) vals[i] = i;

        Solution.ListNode head = buildList(vals, -1);
        assertThat(solution.hasCycle(head)).isFalse();
    }

    @Test
    @DisplayName("list of 1000 nodes (maximum length per constraints), cycle back to head")
    void maxLengthListWithCycle() {
        int[] vals = new int[1000];
        for (int i = 0; i < 1000; i++) vals[i] = i;

        Solution.ListNode head = buildList(vals, 0);
        assertThat(solution.hasCycle(head)).isTrue();
    }
}
