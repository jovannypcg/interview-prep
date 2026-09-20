| | |
|---|---|
| **Solved on** | 2026-09-20 |
| **DSA Category** | Linked List |

## 1. Your Solution Assessment

**Correctness:** Correct on all cases, including single-node lists, head removal, tail removal, and lists with duplicate values (where the fix has to key off *position*, not value). The `head == null || n <= 0` guard is unreachable under the stated constraints (`1 <= n <= sz`, `sz >= 1`), but it's harmless defensive code, not a bug.

**Code quality:** Clean, idiomatic single-pass two-pointer solution with a dummy node to avoid special-casing head removal. Compact multi-variable declaration reads fine here since all four pointers are tightly related.

**Time complexity:** O(sz) — `fast` advances at most `sz` total steps across the initial offset loop and the `while` loop combined; every node is visited a constant number of times.

**Space complexity:** O(1) — only a fixed number of pointers and one dummy node are allocated, regardless of list length.

**Algorithm trace** (annotated array — two pointers)
Input: `head = [1, 2, 3, 4, 5]`, `n = 2` (`prev` starts at the dummy node, before the array)

```
Advance fast 2 steps ahead of slow:
[1, 2, 3, 4, 5]
 S     F

fast != null → shift prev/slow/fast right
[1, 2, 3, 4, 5]
 P  S     F

fast != null → shift right again
[1, 2, 3, 4, 5]
    P  S     F

fast != null → shift right again; fast falls off the list → stop
[1, 2, 3, 4, 5]
       P  S
```
`prev` (value 3) → `slow` (value 4) → unlink: `prev.next = slow.next`
→ return `[1, 2, 3, 5]`

## 2. Optimal Approach

This *is* the optimal approach: a single-pass two-pointer technique with a dummy node.

- Point `fast` and `slow` at `head`, with `prev` at a `dummy` node placed before `head`.
- Advance `fast` alone by `n` nodes, opening a fixed `n`-node gap between `fast` and `slow`.
- Advance `prev`, `slow`, and `fast` together, one step at a time, until `fast` runs off the end of the list. Because the gap is fixed, `slow` now sits exactly on the node to remove, and `prev` sits on the node right before it.
- Unlink `slow`: `prev.next = slow.next`.
- Return `dummy.next` (handles the case where `head` itself was removed).

**Time complexity:** O(sz) — one pass to open the gap, one pass to close it; no node is visited more than twice.
**Space complexity:** O(1) — no auxiliary data structure, just a handful of pointers.

```java
public ListNode removeNthFromEnd(ListNode head, int n) {
    ListNode dummy = new ListNode(-1, head);
    ListNode prev = dummy;
    ListNode slow = head;
    ListNode fast = head;

    for (int i = 0; i < n; i++) {
        fast = fast.next;
    }

    while (fast != null) {
        prev = slow;
        slow = slow.next;
        fast = fast.next;
    }

    prev.next = slow.next;

    return dummy.next;
}
```

**Algorithm trace:** identical to the trace in section 1 — this is the approach the user implemented.

## 3. Alternative Approaches

### 3.1 Two-pass counting
Traverse once to count the list length `sz`, then traverse again to the node just before index `sz - n` (0-indexed from the front) and unlink its successor.

- **Time:** O(sz) — two full traversals, still linear.
- **Space:** O(1) — no extra structures.
- **When acceptable:** A natural fallback if the single-pass trick doesn't come to mind under interview pressure; still meets the optimal time complexity, just with a larger constant factor.

**Algorithm trace** (step table — iterative loop)
Input: `head = [1, 2, 3, 4, 5]`, `n = 2`

| Step | Action | sz | target index (0-indexed, from front) | pointer position |
|---|---|---|---|---|
| 1 | Count nodes | 5 | — | — |
| 2 | Walk `sz - n - 1 = 2` steps from dummy | — | 3 | at value 3 |
| 3 | Unlink | — | — | `prev.next = prev.next.next` (skips value 4) |

→ return `[1, 2, 3, 5]`

### 3.2 Recursive (count from the tail)
Recurse to the end of the list, then unwind while incrementing a counter; when the counter equals `n`, the *current* frame's node is the one to remove, so splice it out via the caller's `next` reference (commonly done with an `int[1]` counter or a small wrapper class, since Java can't return two values).

- **Time:** O(sz) — every node is visited once on the way down and once on the way up.
- **Space:** O(sz) — the call stack grows with list length (not O(1), unlike the iterative versions).
- **When acceptable:** Fine for small lists (this problem caps `sz` at 30) or when demonstrating recursive thinking; risky for very long lists due to stack depth.

**Algorithm trace** (call stack table)
Input: `head = [1, 2, 3, 4, 5]`, `n = 2`, `helper(node)` returns the node's distance from the end

| Depth | Call | Returns / Action |
|---|---|---|
| 0 | helper(1) | waits on helper(2) |
| 1 | helper(2) | waits on helper(3) |
| 2 | helper(3) | waits on helper(4) |
| 3 | helper(4) | waits on helper(5) |
| 4 | helper(5) | waits on helper(null) |
| 5 | helper(null) | returns 0 |
| 4 | helper(5) | returns 1 |
| 3 | helper(4) | returns 2 → **count == n** → unlink: `node(3).next = node(5)` |
| 2 | helper(3) | returns 3 |
| 1 | helper(2) | returns 4 |
| 0 | helper(1) | returns 5 |

→ return `head` (value 1); list is now `[1, 2, 3, 5]`

### 3.3 Convert to array, remove, rebuild
Walk the list once collecting values into an array/list, remove the element at index `sz - n`, then build a brand-new linked list from what remains.

- **Time:** O(sz) — one pass to collect, one pass to rebuild.
- **Space:** O(sz) — the intermediate array/list plus the rebuilt nodes.
- **When acceptable:** Fastest to write correctly under time pressure since there's no pointer-surgery to get wrong, but wasteful in production code and loses the original node objects (a problem if callers hold references to them).

**Algorithm trace** (step table)
Input: `head = [1, 2, 3, 4, 5]`, `n = 2`

| Step | Action | Result |
|---|---|---|
| 1 | Collect values | `[1, 2, 3, 4, 5]` |
| 2 | Remove index `sz - n = 3` | `[1, 2, 3, 5]` |
| 3 | Rebuild linked list | `1 → 2 → 3 → 5` |

→ return `[1, 2, 3, 5]`
