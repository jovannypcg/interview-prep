| | |
|---|---|
| **Solved on** | 2026-09-20 |
| **DSA Category** | Linked List |

## 1. Your Solution Assessment

**Correctness:** Handles all cases correctly — single node, two nodes, even and odd lengths,
duplicate values, and boundary values (`1` and `1000`). Verified against the 50,000-node
maximum-length case as well. No bugs found.

**Code quality:** Clean three-phase structure (find middle → reverse second half → merge) that
mirrors the standard textbook approach for this problem. Variable names (`slow`, `fast`,
`prev`, `first`, `second`) are conventional and easy to follow. The comments now explain the
*why* behind each phase without restating the code.

**Time complexity:** `O(n)` — each of the three phases (find middle, reverse, merge) is a
single linear pass over at most `n` nodes.

**Space complexity:** `O(1)` — only a fixed number of pointer variables are used; the
reordering is done in place by rewiring existing nodes.

**Algorithm trace** (call stack table doesn't apply here — this is iterative, so a step table
is used instead)

Input: `head = [1,2,3,4,5]`

*Phase 1 — find middle (`slow`/`fast`)*

| step | slow | fast |
|---|---|---|
| start | 1 | 2 |
| 1 | 2 | 4 |
| 2 | 3 | null (loop exits) |

`slow` lands on node `3`, the true middle. `second = slow.next = 4`, then `slow.next = null`
splits the list into `1 -> 2 -> 3` and `4 -> 5`.

*Phase 2 — reverse second half (`4 -> 5` becomes `5 -> 4`)*

| step | second | prev |
|---|---|---|
| start | 4 | null |
| 1 | 5 | 4 |
| 2 | null (loop exits) | 5 |

*Phase 3 — merge (`first` starts at `1`, `second` starts at `prev = 5`)*

| step | first (before) | second (before) | first.next set to | second.next set to |
|---|---|---|---|---|
| 1 | 1 | 5 | 5 | 2 |
| 2 | 2 | 4 | 4 | 3 |
| 3 | 3 | null (loop exits) | — | — |

→ final list: `1 -> 5 -> 2 -> 4 -> 3 -> null`

## 2. Optimal Approach

This *is* the optimal approach. The problem forces `O(n)` time (every node must be visited to
rewire it) and the challenge is doing it in `O(1)` extra space instead of the easier `O(n)`
space alternative (below). The three-phase technique — middle-finding with slow/fast pointers,
in-place reversal, in-place alternating merge — is the standard optimal solution and is exactly
what's implemented.

```java
public void reorderList(ListNode head) {
    if (head == null) return;

    ListNode slow = head;
    ListNode fast = head.next;

    while (fast != null && fast.next != null) {
        slow = slow.next;
        fast = fast.next.next;
    }

    ListNode second = slow.next;
    slow.next = null;

    ListNode prev = null;
    while (second != null) {
        ListNode temp = second.next;
        second.next = prev;
        prev = second;
        second = temp;
    }

    ListNode first = head;
    second = prev;

    while (second != null) {
        ListNode temp1 = first.next, temp2 = second.next;
        first.next = second;
        second.next = temp1;
        first = temp1;
        second = temp2;
    }
}
```

**Time complexity:** `O(n)` — three linear passes.
**Space complexity:** `O(1)` — pointer rewiring only, no auxiliary data structure.

**Algorithm trace:** same as the one in Section 1 above (the user's solution already
implements this optimal approach).

## 3. Alternative Approaches

### A. Array / list buffer

Copy every node reference into an `ArrayList<ListNode>` in one pass, then use two indices
(`i = 0`, `j = n - 1`) walking toward each other, relinking `next` pointers as
`nodes[i] -> nodes[j] -> nodes[i+1] -> nodes[j-1] -> ...` until they meet.

**Time complexity:** `O(n)` — one pass to fill the array, one pass to relink.
**Space complexity:** `O(n)` — the array holds a reference to every node.
**When acceptable:** Under interview time pressure, since it avoids the fiddly reverse/merge
logic and is easy to get right on the first try. Reasonable as a fallback if the `O(1)`-space
approach isn't coming together in time.

**Algorithm trace** (annotated array)

Input: `nodes = [1, 2, 3, 4, 5]` (indices 0–4)

```
i=0 j=4  link 1 -> 5
[1, 2, 3, 4, 5]
 i           j

i=1 j=3  link 5 -> 2, then 2 -> 4
[1, 2, 3, 4, 5]
    i     j

i=2 j=2  i == j → link 4 -> 3, stop
[1, 2, 3, 4, 5]
       i,j
```
→ final list: `1 -> 5 -> 2 -> 4 -> 3 -> null`

### B. Deque of node references

Push every node into an `ArrayDeque<ListNode>` while traversing. Then repeatedly
`pollFirst()`/`pollLast()`, alternating, and relink `next` pointers in that order until the
deque is empty (an odd-length list leaves one final `pollFirst()` for the middle node).

**Time complexity:** `O(n)` — one pass to fill the deque, one pass to drain it.
**Space complexity:** `O(n)` — the deque holds a reference to every node.
**When acceptable:** Functionally identical to approach A; some find the deque's
`pollFirst`/`pollLast` symmetry easier to reason about than manual index bookkeeping. Same
interview-time-pressure justification applies.

**Algorithm trace** (annotated array, deque shown as the same array with both ends shrinking)

Input: `nodes = [1, 2, 3, 4, 5]`

```
pollFirst() -> 1        pollLast() -> 5        link 1 -> 5
[1, 2, 3, 4, 5]
 ^front            ^back

pollFirst() -> 2        pollLast() -> 4        link 5 -> 2, 2 -> 4
[_, 2, 3, 4, _]
    ^front      ^back

pollFirst() -> 3 (deque now empty)             link 4 -> 3
[_, _, 3, _, _]
       ^front,back
```
→ final list: `1 -> 5 -> 2 -> 4 -> 3 -> null`

### C. Recursive reorder (not recommended)

Recursively pair the current node with the node found by walking to the end of the remaining
sublist, unwinding the call stack to relink pointers. This works but is easy to get subtly
wrong (stopping conditions for odd/even lengths, avoiding cycles) and each recursive call still
does an `O(n)` walk to find its partner node.

**Time complexity:** `O(n^2)` — for each of the `n/2` pairings, finding the partner node
requires an `O(n)` walk from the current position to the end.
**Space complexity:** `O(n)` — recursion depth is `O(n)`, and each frame retains references
needed to relink pointers on the way back up.
**When acceptable:** Not recommended even under time pressure — it's slower than approaches A
and B while being harder to implement correctly, with no offsetting benefit.

**Algorithm trace** (call stack table)

Input: `head = [1,2,3,4,5]` — each call relinks the current node to the tail of the remaining
sublist, then recurses on the shrunken middle sublist.

| Depth | Call (sublist) | Action | Returns |
|---|---|---|---|
| 0 | reorder([1,2,3,4,5]) | walk to tail (5), link 1 -> 5 | reorder([2,3,4]) |
| 1 | reorder([2,3,4]) | walk to tail (4), link 2 -> 4 | reorder([3]) |
| 2 | reorder([3]) | single node, base case | link 4 -> 3, stop |
→ final list: `1 -> 5 -> 2 -> 4 -> 3 -> null`
