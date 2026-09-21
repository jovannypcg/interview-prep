| | |
|---|---|
| **Solved on** | 2026-09-21 |
| **DSA Category** | Linked List |

## 1. Your Solution Assessment

### Correctness

Handles all cases correctly:

- Equal-length lists (Example 1, 3).
- Unequal-length lists in both directions (Example 5, 6).
- Single-digit carry that creates an extra node (Example 4, 7).
- Multiple consecutive carries rippling through (Example 7: `99 + 1 = 100`).
- Both lists reduced to `[0]` (Example 2).
- Maximum-length lists (100 nodes each, all 9s).

The `if (l1 == null) return l2; if (l2 == null) return l1;` guards are unreachable given the
problem's constraints (`1 <= nodes.length`), but they don't cause any incorrect behavior — just
dead code.

### Code Quality

- Naming is clear (`currOut`, `currL1`, `currL2`, `carry`, `res`) and consistent across the three
  loops.
- The dummy-node pattern is used correctly to avoid special-casing the head of the output list.
- The main weakness is **duplication**: the digit-extraction-and-carry logic (`sum`, `res`,
  `carry = sum / 10`) is repeated near-verbatim across three separate `while` loops. This is
  functionally correct but harder to maintain — a bug fix in the arithmetic would need to be
  applied in three places. The Idiomatic Version below shows how to collapse this into a single
  loop.

### Time Complexity

`O(max(m, n))`, where `m` and `n` are the lengths of `l1` and `l2`. Each node from both lists is
visited exactly once across the three loops combined, plus at most one extra step for a trailing
carry.

### Space Complexity

`O(max(m, n))` for the newly allocated output list (required by the problem, since the result
must be returned as a list). No additional auxiliary space is used beyond a few pointers and
counters.

### Algorithm Trace

**Step table** — Input: `l1 = [2,4,3]` (342), `l2 = [5,6,4]` (465) — Example 1

| Phase | currL1.val | currL2.val | carry (before) | sum | res | carry (after) | output so far |
|---|---|---|---|---|---|---|---|
| 1 | 2 | 5 | 0 | 7 | 7 | 0 | [7] |
| 1 | 4 | 6 | 0 | 10 | 0 | 1 | [7,0] |
| 1 | 3 | 4 | 1 | 8 | 8 | 0 | [7,0,8] |

Both lists exhausted simultaneously, `carry == 0` at the end, so phases 2/3 and the trailing-carry
check are skipped.
→ return `[7, 0, 8]`

### Idiomatic Version

The three-loop structure can be collapsed into a single loop by treating a missing node as digit
`0` and continuing while either list still has nodes **or** a carry remains:

```java
public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
    ListNode dummy = new ListNode(0);
    ListNode curr = dummy;
    int carry = 0;

    while (l1 != null || l2 != null || carry != 0) {
        int x = (l1 != null) ? l1.val : 0;
        int y = (l2 != null) ? l2.val : 0;
        int sum = x + y + carry;

        carry = sum / 10;
        curr.next = new ListNode(sum % 10);
        curr = curr.next;

        if (l1 != null) l1 = l1.next;
        if (l2 != null) l2 = l2.next;
    }

    return dummy.next;
}
```

This removes the code duplication, drops the need for the unreachable null guards (a `null` list
is naturally treated as contributing `0`), and folds the trailing-carry handling into the same
loop condition instead of a separate `if` afterward. Same `O(max(m, n))` time and space
complexity — this is a readability improvement, not a performance one.

## 2. Optimal Approach

This problem's optimal approach **is** the single-pass simulation shown above: walk both lists
once, add corresponding digits plus any carry, and emit one output digit per step. There is no
asymptotically faster approach, since every digit of both numbers must be read at least once to
produce a correct sum.

- **Time complexity:** `O(max(m, n))` — one pass over the longer list; the shorter list
  contributes `0` once exhausted.
- **Space complexity:** `O(max(m, n))` — the output list has at most `max(m, n) + 1` nodes (the
  `+1` only when a final carry survives).

```java
public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
    ListNode dummy = new ListNode(0);
    ListNode curr = dummy;
    int carry = 0;

    while (l1 != null || l2 != null || carry != 0) {
        int x = (l1 != null) ? l1.val : 0;
        int y = (l2 != null) ? l2.val : 0;
        int sum = x + y + carry;

        carry = sum / 10;
        curr.next = new ListNode(sum % 10);
        curr = curr.next;

        if (l1 != null) l1 = l1.next;
        if (l2 != null) l2 = l2.next;
    }

    return dummy.next;
}
```

### Algorithm Trace

**Step table** — Input: `l1 = [9,9]` (99), `l2 = [1]` (1) — Example 7

| Iteration | l1 present? | l2 present? | x | y | carry (before) | sum | carry (after) | digit emitted |
|---|---|---|---|---|---|---|---|---|
| 1 | yes | yes | 9 | 1 | 0 | 10 | 1 | 0 |
| 2 | yes | no | 9 | 0 | 1 | 10 | 1 | 0 |
| 3 | no | no | 0 | 0 | 1 | 1 | 0 | 1 |

Loop condition `l1 != null || l2 != null || carry != 0` keeps it running for a third iteration
purely to flush the trailing carry — no separate post-loop `if` needed.
→ return `[0, 0, 1]`

## 3. Alternative Approaches

### Convert to integers, add, convert back

Read each list into an actual number (accumulating with increasing place value, since digits are
stored least-significant-first), add the two numbers with normal arithmetic, then split the sum
back into a new list of digits.

- **Time complexity:** `O(m + n)` to build both numbers plus `O(log(sum))` to re-split the
  result — effectively `O(max(m, n))`.
- **Space complexity:** `O(max(m, n))` for the output list; `O(1)` extra beyond that.
- **When acceptable:** Fine for small inputs, but risky in general — a plain `long` overflows
  once the numbers exceed ~18-19 digits, and the constraint allows up to 100 digits. Using
  `BigInteger` avoids overflow but adds conversion overhead and is generally seen as "cheating"
  the linked-list traversal the problem is testing. Reasonable to mention in an interview as a
  quick sanity-check approach, not as the final answer.

```java
public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
    java.math.BigInteger n1 = toNumber(l1);
    java.math.BigInteger n2 = toNumber(l2);
    String sum = n1.add(n2).toString();

    ListNode dummy = new ListNode(0);
    ListNode curr = dummy;
    for (int i = sum.length() - 1; i >= 0; i--) {
        curr.next = new ListNode(sum.charAt(i) - '0');
        curr = curr.next;
    }
    return dummy.next;
}

private java.math.BigInteger toNumber(ListNode node) {
    StringBuilder sb = new StringBuilder();
    while (node != null) {
        sb.append(node.val);
        node = node.next;
    }
    return new java.math.BigInteger(sb.reverse().toString());
}
```

#### Algorithm Trace

**Step table** — Input: `l1 = [2,4,3]`, `l2 = [5,6,4]` — Example 1

| Step | Action | Value |
|---|---|---|
| 1 | Read `l1` digits in list order | `2, 4, 3` |
| 2 | Reverse to get most-significant-first | `"342"` |
| 3 | Read `l2` digits, reverse | `"465"` |
| 4 | `342 + 465` | `807` |
| 5 | Split `"807"` back into digits, reversed | `[7, 0, 8]` |
→ return `[7, 0, 8]`

### Recursive digit-by-digit addition

Process both lists one node at a time via recursion, passing the carry as a parameter, and build
the result on the way back up (or down, prepending/appending depending on structure). Requires a
helper method since the public signature has no `carry` parameter.

- **Time complexity:** `O(max(m, n))` — same single pass as the iterative version, just expressed
  as recursive calls.
- **Space complexity:** `O(max(m, n))` **call-stack** space in addition to the output list, since
  each recursive call stays on the stack until its subcall returns.
- **When acceptable:** Reasonable under interview time pressure if recursion feels more natural to
  reason about, but the iterative version is strictly better here — no risk of stack overflow
  (the constraint allows up to 100 nodes, which is safe either way, but recursion doesn't scale
  as cleanly if that bound were larger).

```java
public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
    return addWithCarry(l1, l2, 0);
}

private ListNode addWithCarry(ListNode l1, ListNode l2, int carry) {
    if (l1 == null && l2 == null && carry == 0) return null;

    int x = (l1 != null) ? l1.val : 0;
    int y = (l2 != null) ? l2.val : 0;
    int sum = x + y + carry;

    ListNode node = new ListNode(sum % 10);
    node.next = addWithCarry(
        l1 != null ? l1.next : null,
        l2 != null ? l2.next : null,
        sum / 10
    );
    return node;
}
```

#### Algorithm Trace

**Call stack table** — Input: `l1 = [5]`, `l2 = [5]` — Example 4

| Depth | Call | Returns |
|---|---|---|
| 0 | `addWithCarry([5], [5], 0)` | node(0) → `addWithCarry(null, null, 1)` |
| 1 | `addWithCarry(null, null, 1)` | node(1) → `addWithCarry(null, null, 0)` |
| 2 | `addWithCarry(null, null, 0)` | `null` |
→ result: `[0, 1]`
