# Linked List Cycle Detection — Review

| | |
|---|---|
| **Solved on** | 2026-09-19 |
| **DSA Category** | Linked List |

---

## 1. Your Solution Assessment

**Correctness:** Correct. Verified against all 7 README examples and all 13 tests in
`SolutionTest.java`, including the traps this problem sets:

- **Self-loop** (`head = [1]`, `index = 0`): `fast` starts at `head.next`, which is `head`
  itself here, so the loop condition (`fast != null && fast.next != null`) is satisfied on
  entry and `slow == fast` is true immediately — detected correctly on the first check.
- **No false positive on entry:** starting `fast` at `head.next` (instead of `head`) avoids the
  bug of comparing `slow == fast` before either pointer has moved, which would otherwise report
  a cycle for *every* non-empty list.
- **Duplicate values** (`[7,7,7,7]`): the comparison is `slow == fast`, i.e., reference
  identity, not `.val` equality — so lists full of equal values without a cycle are correctly
  reported as `false`.

**Code quality:** Clean and idiomatic — `slow`/`fast` names communicate the tortoise-and-hare
pattern without comments, and the `head == null` guard handles the empty-list edge case
explicitly before the loop even starts.

**Time complexity: O(n)** — `fast` moves twice as fast as `slow`; if there's no cycle, `fast`
reaches `null` after at most n/2 iterations. If there is a cycle, `fast` catches up to `slow`
within at most one full loop around the cycle, which is bounded by n.

**Space complexity: O(1)** — only two pointer variables, regardless of list length.

**Algorithm trace** (step table) — Input: `head = [3,2,0,-4]`, `index = 1` (tail `-4` connects
back to node `2`)

Nodes by position: `N0=3 → N1=2 → N2=0 → N3=-4 ⤴ N1`

| Step | slow | fast | slow == fast? | Action |
|---|---|---|---|---|
| Start | N0 (3) | N1 (2) | — | loop entry check: `fast` and `fast.next` both non-null |
| 1 | N0 (3) | N1 (2) | No | `slow → N1 (2)`, `fast → N3 (-4)` |
| 2 | N1 (2) | N3 (-4) | No | `slow → N2 (0)`, `fast → N2 (0)` |
| 3 | N2 (0) | N2 (0) | **Yes** | return `true` |

---

## 2. Optimal Approach

Your solution **is** the optimal approach. Floyd's cycle detection (tortoise and hare) finds a
cycle in a single pass with constant extra space — no hash set, no mutation of the list.

**Time complexity: O(n)** — one traversal; the two pointers meet within one lap of the cycle if
one exists.

**Space complexity: O(1)** — two pointer variables only.

```java
public boolean hasCycle(ListNode head) {
    if (head == null) return false;

    ListNode slow = head;
    ListNode fast = head.next;

    while (fast != null && fast.next != null) {
        if (slow == fast) return true;

        slow = slow.next;
        fast = fast.next.next;
    }

    return false;
}
```

**Algorithm trace** (step table) — Input: `head = [1,2,3,4,5,6]`, `index = 2` (tail `6` connects
back to node `3`)

Nodes by position: `N0=1 → N1=2 → N2=3 → N3=4 → N4=5 → N5=6 ⤴ N2`

| Step | slow | fast | slow == fast? | Action |
|---|---|---|---|---|
| Start | N0 (1) | N1 (2) | — | loop entry check passes |
| 1 | N0 (1) | N1 (2) | No | `slow → N1 (2)`, `fast → N3 (4)` |
| 2 | N1 (2) | N3 (4) | No | `slow → N2 (3)`, `fast → N5 (6)` |
| 3 | N2 (3) | N5 (6) | No | `slow → N3 (4)`, `fast → N3 (4)` (fast wrapped: `N5.next=N2`, `N2.next=N3`) |
| 4 | N3 (4) | N3 (4) | **Yes** | return `true` |

---

## 3. Alternative Approaches

### Hash set of visited node references

Traverse the list, adding each node **reference** (not value) to a `HashSet<ListNode>` as you
visit it. If you ever land on a node already in the set, there's a cycle; if you reach `null`
first, there isn't.

**Time complexity: O(n)** — each node is visited once, with O(1) amortized set insert/lookup.

**Space complexity: O(n)** — the set holds a reference to every distinct node visited before a
cycle is found (or the list ends).

**When acceptable:** A perfectly fine first correct answer under interview pressure, or when you
also need to know *where* the cycle begins — the classic follow-up ("Linked List Cycle II")
builds on this same idea before introducing Floyd's second phase.

**Algorithm trace** — Input: `head = [3,2,0,-4]`, `index = 1`

| Node visited | val | already in set? | seen so far |
|---|---|---|---|
| N0 | 3 | No | `{N0}` |
| N1 | 2 | No | `{N0, N1}` |
| N2 | 0 | No | `{N0, N1, N2}` |
| N3 | -4 | No | `{N0, N1, N2, N3}` |
| N1 (again, via back-edge) | 2 | **Yes** | — |
→ return `true`

### Destructive marking (redirect visited nodes to a sentinel)

Traverse the list, and after visiting each node, redirect its `next` pointer to a dedicated
sentinel node. If you ever arrive at that sentinel while traversing, a cycle exists; if you
reach the original `null` first, it doesn't.

**Time complexity: O(n)** — one pass, one pointer redirect per node.

**Space complexity: O(1)** extra — only the sentinel node itself, no per-node storage.

**When acceptable:** Rarely — it permanently destroys the input list's structure, which is
unacceptable whenever the caller expects the list to remain usable afterward (the norm for this
problem). Only reasonable if the list is being discarded immediately after the check.

**Algorithm trace** — Input: `head = [1,2]`, `index = -1` (no cycle)

| Step | Node visited | Action | Hit sentinel? |
|---|---|---|---|
| 1 | N0 (1) | `N0.next → SENTINEL` (saving original `N1` to continue traversal) | No |
| 2 | N1 (2) | `N1.next → SENTINEL` (original `next` was already `null`) | No |
| 3 | — | traversal reaches `null` (no sentinel hit) | — |
→ return `false`
