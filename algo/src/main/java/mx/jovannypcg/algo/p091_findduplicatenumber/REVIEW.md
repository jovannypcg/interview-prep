| | |
|---|---|
| **Solved on** | 2026-09-22 |
| **DSA Category** | Two Pointers |

## 1. Your Solution Assessment

**Correctness:** Handles all cases correctly, verified by 10 passing unit tests — the minimal
case (`n = 1`), the maximum-value and minimum-value duplicate positions, a duplicate repeated
more than twice, a large `n = 100,000` array, and a no-mutation check confirming `nums` is
left untouched. No bugs found.

**Code quality:** Clean two-phase structure that matches the standard Floyd's cycle-detection
shape. The comments now explain *why* each phase exists (detect the cycle, then find its
entrance) rather than restating the code line by line. Variable names (`slow`, `fast`) are
conventional for this pattern and easy to follow.

**Time complexity:** `O(n)` — each phase is a bounded walk over the implicit linked list built
from `nums`, and the walk length is proportional to the tail length plus the cycle length,
both of which are at most `n`.

**Space complexity:** `O(1)` — only two `int` pointer variables are used regardless of input
size.

**Algorithm trace** (annotated array — two pointers)

Input: `nums = [1, 3, 4, 2, 2]` (indices 0–4; `nums[i]` is treated as "index `i` points to
index `nums[i]`")

*Phase 1 — find a meeting point inside the cycle*
```
start: slow = nums[0] = 1, fast = nums[nums[0]] = nums[1] = 3
[1, 3, 4, 2, 2]
 s     f

step 1: slow = nums[1] = 3, fast = nums[nums[3]] = nums[2] = 4
[1, 3, 4, 2, 2]
       f  s

step 2: slow = nums[3] = 2, fast = nums[nums[4]] = nums[2] = 4
[1, 3, 4, 2, 2]
       f     s

step 3: slow = nums[2] = 4, fast = nums[nums[4]] = nums[2] = 4
[1, 3, 4, 2, 2]
       s=f
→ meeting point = 4
```

*Phase 2 — reset slow to index 0, walk both 1 step at a time*
```
start: slow = 0, fast = 4
[1, 3, 4, 2, 2]
 s        f

step 1: slow = nums[0] = 1, fast = nums[4] = 2
[1, 3, 4, 2, 2]
    s     f

step 2: slow = nums[1] = 3, fast = nums[2] = 4
[1, 3, 4, 2, 2]
       f  s

step 3: slow = nums[3] = 2, fast = nums[4] = 2
[1, 3, 4, 2, 2]
       s=f
→ return 2
```

## 2. Optimal Approach

This **is** the optimal approach — your implementation already matches it.

**Explanation:** Treat `nums` as a functional graph: index `i` has an edge to index `nums[i]`.
Since every value is in `[1, n]` and there are `n + 1` indices `[0, n]`, index `0` can never be
pointed to (no value is `0`), so it sits on a "tail" leading into a cycle. Because the
duplicate value is written at two different indices, the node representing that value has two
incoming edges — that's exactly what forces a cycle to exist, and the duplicate value is the
node at the cycle's entrance. Finding it is now identical to the classic "find the start of the
cycle in a linked list" problem (LeetCode 142), solved with Floyd's tortoise-and-hare
algorithm in two phases: detect the cycle, then locate its entrance.

**Time complexity:** `O(n)` — phase 1 takes at most `O(n)` steps for `slow` to enter the cycle
and for `fast` to lap it; phase 2 takes at most `O(n)` more steps.

**Space complexity:** `O(1)` — two pointers only, no auxiliary data structures, and `nums` is
never mutated.

```java
public int findDuplicate(int[] nums) {
    int slow = nums[0];
    int fast = nums[nums[0]];

    while (slow != fast) {
        slow = nums[slow];
        fast = nums[nums[fast]];
    }

    slow = 0;
    while (slow != fast) {
        slow = nums[slow];
        fast = nums[fast];
    }

    return slow;
}
```

### Why the slow/fast pointers meet at the duplicate (distance proof)

Let:
- `μ` (mu) = the tail length — the number of steps from index `0` to the cycle's entrance.
- `λ` (lambda) = the cycle length.

Think of phase 1 as `t` synchronized steps starting from index `0`, where after `t` steps
`slow` has moved `t` steps and `fast` has moved `2t` steps (your pre-advanced initialization is
just this do-while loop unrolled by one iteration).

**Step 1 — they must meet on a multiple of `λ`.**
Once both pointers are inside the cycle, their position *within* the cycle is their total
steps minus the tail, taken mod `λ`. They meet when:

```
(t - μ) ≡ (2t - μ)  (mod λ)
        -t ≡ 0       (mod λ)
         t ≡ 0        (mod λ)
```

So the meeting happens after `t = kλ` steps of `slow`, for some integer `k` large enough that
`slow` has already entered the cycle (`t ≥ μ`).

**Step 2 — where inside the cycle they meet.**
The meeting point's distance from the cycle entrance is:

```
(t - μ) mod λ  =  (kλ - μ) mod λ  =  (-μ) mod λ
```

In other words, the meeting point is exactly `μ` steps *before* the entrance, measured going
forward around the cycle.

**Step 3 — why resetting `slow` to the head works.**
In phase 2, `slow` restarts at index `0` (`μ` steps away from the entrance, by definition of
`μ`) and `fast` stays at the meeting point (`(-μ) mod λ` steps away from the entrance, i.e. it
needs exactly `μ` more forward steps to complete the loop back to the entrance). Advancing both
by 1 step at a time for `μ` steps:

```
slow:  0 + μ           = μ                      → lands exactly on the entrance
fast:  (-μ mod λ) + μ  ≡ 0 (mod λ)               → also lands exactly on the entrance
```

Both expressions land on the same node after exactly `μ` steps — the cycle's entrance — which
is precisely the duplicate value, since it's the only node in the graph with two incoming
edges (two different indices map to it).

## 3. Alternative Approaches

### Sorting

Copy `nums`, sort the copy, and scan for two adjacent equal elements.

- **Time:** `O(n log n)` — dominated by the sort.
- **Space:** `O(n)` — a copy is required since the original `nums` cannot be modified.
- **When acceptable:** Fine under interview time pressure as a first correct pass, or when the
  constant-space constraint is relaxed; it's easy to explain and hard to get wrong.

```java
public int findDuplicate(int[] nums) {
    int[] sorted = nums.clone();
    Arrays.sort(sorted);

    for (int i = 1; i < sorted.length; i++) {
        if (sorted[i] == sorted[i - 1]) return sorted[i];
    }

    return -1;
}
```

**Trace** (step table) — `nums = [1, 3, 4, 2, 2]` → sorted = `[1, 2, 2, 3, 4]`

| i | sorted[i] | sorted[i-1] | duplicate found? |
|---|---|---|---|
| 1 | 2 | 1 | No |
| 2 | 2 | 2 | **Yes → return 2** |

### Hash Set

Walk `nums` once, tracking seen values in a `HashSet`; return the first value seen twice.

- **Time:** `O(n)` — one pass, O(1) average set operations.
- **Space:** `O(n)` — the set can hold up to `n` values.
- **When acceptable:** Violates the constant-space constraint, but it's the simplest correct
  solution and a reasonable warm-up before optimizing to Floyd's algorithm.

```java
public int findDuplicate(int[] nums) {
    Set<Integer> seen = new HashSet<>();

    for (int num : nums) {
        if (!seen.add(num)) return num;
    }

    return -1;
}
```

**Trace** (step table) — `nums = [1, 3, 4, 2, 2]`

| i | nums[i] | already in set? | set after |
|---|---|---|---|
| 0 | 1 | No | {1} |
| 1 | 3 | No | {1, 3} |
| 2 | 4 | No | {1, 3, 4} |
| 3 | 2 | No | {1, 3, 4, 2} |
| 4 | 2 | **Yes** | — return 2 |

### Binary Search on the value range

Binary search over candidate answers `[1, n]` instead of over the array. For a midpoint `m`,
count how many elements of `nums` are `≤ m`. If that count exceeds `m`, the pigeonhole
principle guarantees the duplicate is in `[1, m]`; otherwise it's in `[m + 1, n]`.

- **Time:** `O(n log n)` — `log n` iterations, each doing an `O(n)` count pass.
- **Space:** `O(1)` — only counters and bounds, and `nums` is never mutated.
- **When acceptable:** A fully valid alternative under this problem's exact constraints
  (no mutation, constant space) when `O(n log n)` time is acceptable — useful to know as a
  second constant-space option besides Floyd's algorithm.

```java
public int findDuplicate(int[] nums) {
    int lo = 1, hi = nums.length - 1;

    while (lo < hi) {
        int mid = lo + (hi - lo) / 2;
        int count = 0;

        for (int num : nums) {
            if (num <= mid) count++;
        }

        if (count > mid) hi = mid;
        else lo = mid + 1;
    }

    return lo;
}
```

**Trace** (step table + annotated range) — `nums = [1, 3, 4, 2, 2]`, `n = 4`

```
Search range [1, 4]
lo=1  hi=4  mid=2  count(≤2)=3  →  3 > 2  →  duplicate in [1, 2], hi=2

Search range [1, 2]
lo=1  hi=2  mid=1  count(≤1)=1  →  1 ≤ 1  →  duplicate in [2, 2], lo=2

lo == hi == 2 → return 2
```

| lo | hi | mid | count(≤mid) | count > mid? | action |
|---|---|---|---|---|---|
| 1 | 4 | 2 | 3 | Yes | hi = 2 |
| 1 | 2 | 1 | 1 | No | lo = 2 |
| 2 | 2 | — | — | loop ends | return 2 |

### Negative marking (index-as-hash)

For each value `v` encountered, visit index `|v|` and negate the value stored there; if it's
already negative, `|v|` is the duplicate.

- **Time:** `O(n)` — one pass, `O(1)` work per element.
- **Space:** `O(1)` — no auxiliary structures.
- **When acceptable:** Almost never acceptable *as written* here — it directly violates "do
  not modify `nums`" during execution, even if restored afterward. Worth knowing only to
  explain why it's excluded, or if an interviewer explicitly relaxes the no-mutation rule.

```java
public int findDuplicate(int[] nums) {
    for (int num : nums) {
        int idx = Math.abs(num);
        if (nums[idx] < 0) return idx;
        nums[idx] = -nums[idx];
    }

    return -1;
}
```

**Trace** (step table) — `nums = [1, 3, 4, 2, 2]`

| i | nums[i] before | idx=&#124;nums[i]&#124; | nums[idx] before | action |
|---|---|---|---|---|
| 0 | 1 | 1 | 3 (pos) | mark nums[1] = -3 |
| 1 | -3 | 3 | 2 (pos) | mark nums[3] = -2 |
| 2 | 4 | 4 | 2 (pos) | mark nums[4] = -2 |
| 3 | -2 | 2 | 4 (pos) | mark nums[2] = -4 |
| 4 | -2 | 2 | -4 (neg) | **duplicate = 2** |
