# Linked List Cycle II — Review

| | |
|---|---|
| **Solved on** | 2026-09-20 |
| **DSA Category** | Linked List |

---

## 1. Your Solution Assessment

**Correctness:** Correct. Verified against all 7 README examples and all 13 tests in
`SolutionTest.java`, including the traps this problem sets:

- **Entry identification, not just detection:** unlike Cycle I, the set must return *which*
  node was the first repeat, not just whether one exists — `return current;` on the first
  `visited.contains(current)` hit does exactly that, and it's correct because the first node
  you revisit while doing a plain forward traversal is always the cycle's entry point.
- **Self-loop** (`head = [1]`, `pos = 0`): the single node is added to `visited`, then
  `current = current.next` loops back to itself, `visited.contains(current)` is true, and the
  same node is returned — correct.
- **Duplicate values** (`[7,7,7,7]`): correctness here hinges on `ListNode` identity, not value
  — see the dedicated note below.
- **Empty list / null head:** the `head == null` guard returns `null` before the set is even
  created.

**Code quality:** Clean and readable — `visited`/`current` names are self-explanatory, and the
loop structure (check → mark → advance) is the simplest possible shape for this idea.

**Time complexity: O(n)** — each node is visited once; `HashSet` insert/lookup is O(1) amortized.

**Space complexity: O(n)** — the set holds a reference to every node visited before a cycle is
found (or the list ends), up to n nodes.

### A note on `Set` vs. an "`IdentityHashSet`"

This is a genuinely good catch, and the reasoning splits into three separate questions:

**Why did a plain `HashSet` work, when `Set` is "known for equality comparison"?**
You're right that `Set` de-duplicates by `equals()`/`hashCode()` — but that contract is defined
by the *elements*, not by `HashSet` itself. `ListNode` never overrides `equals()` or
`hashCode()`, so it inherits `Object`'s defaults: `equals()` is `==` (reference identity), and
`hashCode()` is an identity-derived value (`System.identityHashCode()`, effectively). Since
`ListNode` never opted into value-based equality, `HashSet<ListNode>` behaves like an identity
set here "for free" — not because `HashSet` is identity-aware, but because the element type
gave it no other notion of equality to use. That's exactly why the duplicate-value tests pass:
two distinct nodes both holding `7` are `!equals()` to each other (their identities differ), so
the set tells them apart correctly.

This is also a subtle, easy-to-miss dependency worth flagging: this implementation's correctness
*relies* on `ListNode` never gaining a value-based `equals()`/`hashCode()`. If a future version
of this fixture added one (e.g., to compare lists by value elsewhere), this exact code would
silently break — `[7,7,7,7]` with no cycle would be misreported as cyclic, because the second
`7` node would look "equal" to the first and be treated as already visited.

**Why isn't there a built-in `IdentityHashSet`?**
The JDK collections framework avoids a combinatorial explosion of concrete classes (a `HashSet`
*and* `IdentityHashSet` *and* `LinkedIdentityHashSet`, etc.) by giving you one Map building block
— `IdentityHashMap` — plus a single generic adapter, `Collections.newSetFromMap(Map<E,Boolean>)`,
that turns *any* `Map` into a `Set`. The idiomatic identity set in Java is therefore:

```java
Set<ListNode> visited = Collections.newSetFromMap(new IdentityHashMap<>());
```

`IdentityHashMap` is already called out in its own Javadoc as "not a general-purpose Map
implementation" intended for special-purpose use — so the standard library never gave it the
full family of wrapper types that general-purpose collections get; the `newSetFromMap` adapter
covers that need for every `Map` implementation at once, not just this one.

**When is `IdentityHashMap` (or an identity set) actually the right tool?**
- **Traversing object graphs with a "visited" set** where the node type's `equals()`/
  `hashCode()` are value-based, mutable, or simply not under your control — the exact risk
  above. Deep-copiers, cycle-safe serializers, and clone routines that must key on "have I seen
  this *exact* object" regardless of its value semantics are the classic case (this repo's
  `p068_clonegraph` uses a `HashMap` keyed by node reference for the same reason).
- **Serialization frameworks** — `java.io.ObjectOutputStream` uses identity-based tracking
  internally so that two separately-constructed-but-equal objects aren't accidentally
  deduplicated during serialization.
- **Per-instance tagging/metadata**, e.g. a debugger or profiler attaching data to specific
  object instances without depending on — or being vulnerable to — that object's own
  `equals()`/`hashCode()` contract (including a mutable key whose `hashCode()` changes after
  insertion, which would silently corrupt a regular `HashMap`).
- **Not** the right tool when equality *should* mean "same data" — e.g., `String` or DTO keys —
  since `IdentityHashMap` would treat every distinct-but-equal instance as a different key,
  which is almost always the wrong behavior in that context.

**Algorithm trace** (step table) — Input: `head = [3,2,0,-4]`, `pos = 1` (tail `-4` connects back
to node `2`)

Nodes by position: `N0=3 → N1=2 → N2=0 → N3=-4 ⤴ N1`

| Step | current | in `visited`? | Action |
|---|---|---|---|
| 1 | N0 (3) | No | add N0 → `{N0}` |
| 2 | N1 (2) | No | add N1 → `{N0, N1}` |
| 3 | N2 (0) | No | add N2 → `{N0, N1, N2}` |
| 4 | N3 (-4) | No | add N3 → `{N0, N1, N2, N3}` |
| 5 | N1 (2) | **Yes** | return N1 |
→ return node N1 (value `2`)

---

## 2. Optimal Approach

Floyd's cycle detection (tortoise and hare), extended with a second phase to locate the entry
point, finds the answer in one-and-a-bit passes with **constant** extra space instead of O(n).

**Why it works:** let `a` = distance from `head` to the cycle's entry, `b` = distance from the
entry to the point where `slow`/`fast` first meet, and `c` = the remaining cycle length back to
the entry. When they meet, `slow` has traveled `a + b`, and `fast` has traveled `a + b + k(b+c)`
for some `k ≥ 1` (it lapped the cycle `k` extra times). Since `fast` moves twice as fast as
`slow`, `2(a+b) = a+b+k(b+c)`, which simplifies to `a = k(b+c) - b`, i.e. `a ≡ -b (mod (b+c))`.
That means: starting one pointer at `head` and another at the meeting point, and advancing both
one step at a time, they're guaranteed to meet again exactly at the entry — `a` steps from
`head` lands you there, and `-b` steps from the meeting point (mod cycle length) does too.

**Time complexity: O(n)** — phase 1 (finding the meeting point) is bounded by one traversal plus
at most one extra lap of the cycle; phase 2 (finding the entry) is bounded by `a ≤ n` steps.

**Space complexity: O(1)** — three pointer variables only, list is never modified.

```java
public ListNode detectCycle(ListNode head) {
    if (head == null) return null;

    ListNode slow = head;
    ListNode fast = head;

    while (fast != null && fast.next != null) {
        slow = slow.next;
        fast = fast.next.next;

        if (slow == fast) {
            ListNode ptr = head;

            while (ptr != slow) {
                ptr = ptr.next;
                slow = slow.next;
            }

            return ptr;
        }
    }

    return null;
}
```

**Algorithm trace** (step table) — Input: `head = [3,2,0,-4]`, `pos = 1`

Nodes by position: `N0=3 → N1=2 → N2=0 → N3=-4 ⤴ N1`

*Phase 1 — find the meeting point:*

| Step | slow | fast | slow == fast? |
|---|---|---|---|
| Start | N0 (3) | N0 (3) | — |
| 1 | N1 (2) | N2 (0) | No |
| 2 | N2 (0) | N1 (2) | No |
| 3 | N3 (-4) | N3 (-4) | **Yes** → meet at N3 |

*Phase 2 — walk both from `head` and the meeting point, one step at a time:*

| Step | ptr (from head) | slow (from meeting point) | ptr == slow? |
|---|---|---|---|
| Start | N0 (3) | N3 (-4) | No |
| 1 | N1 (2) | N1 (2) | **Yes** → entry found |
→ return node N1 (value `2`)

---

## 3. Alternative Approaches

### Destructive marking with a sentinel node

Traverse the list; before moving to each node's original successor, redirect that node's `next`
pointer to a shared sentinel object. The first node whose `next` is *already* the sentinel when
you arrive is the cycle's entry (it's the one every path loops back into); reaching a real
`null` first means there's no cycle.

**Time complexity: O(n)** — one pass, one pointer redirect per node.

**Space complexity: O(1)** extra — only the sentinel node, no per-node storage.

**When acceptable:** Essentially never for this problem specifically — it violates the explicit
"do not modify the linked list" constraint, since it mutates every node's `next` pointer (even
if you undid the mutation in a second pass, the list is *temporarily* corrupted, which is
unsafe if anything else could observe it mid-traversal, e.g. concurrent access). It's more of a
conceptual bridge between the hash-set approach and Floyd's — useful for building intuition, not
for production use.

**Algorithm trace** — Input: `head = [1,2,3,4,5,6]`, `pos = 2` (tail `6` connects back to `3`)

| Step | current | original `next` | action | `current.next` already sentinel? |
|---|---|---|---|---|
| 1 | N0 (1) | N1 | `N0.next → SENTINEL` | No |
| 2 | N1 (2) | N2 | `N1.next → SENTINEL` | No |
| 3 | N2 (3) | N3 | `N2.next → SENTINEL` | No |
| 4 | N3 (4) | N4 | `N3.next → SENTINEL` | No |
| 5 | N4 (5) | N5 | `N4.next → SENTINEL` | No |
| 6 | N5 (6) | N2 (back-edge) | `N5.next → SENTINEL` | — |
| 7 | N2 (3) | — | arrived via back-edge; `N2.next` is already `SENTINEL` | **Yes** |
→ return node N2 (value `3`)

### Brute-force linear scan (no hashing)

Same shape as your solution, but replace the `HashSet` with a plain `List<ListNode>` and a
linear `.contains()` scan (`==` per element) instead of a hash-backed lookup.

**Time complexity: O(n²)** — each of the up to n visits does an O(n) linear scan through
previously-seen nodes.

**Space complexity: O(n)** — same storage as the hash-set version, just a worse access pattern.

**When acceptable:** Only for very small inputs, or as the naive "obviously correct" stepping
stone you'd narrate out loud before optimizing to a `HashSet` in an interview — it isolates the
core idea (remember what you've seen; the first repeat is the entry) from the data-structure
choice that makes it fast.

**Algorithm trace** — Input: `head = [1,2]`, `pos = 0` (tail `2` connects back to `1`)

| Step | current | seen list (linear scan) | found? |
|---|---|---|---|
| 1 | N0 (1) | `[]` → not found → append | No |
| 2 | N1 (2) | `[N0]` → not found → append | No |
| 3 | N0 (1) | `[N0, N1]` → scan finds `N0` at index 0 | **Yes** |
→ return node N0 (value `1`)
