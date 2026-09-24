| | |
|---|---|
| **Solved on** | 2026-09-24 |
| **DSA Category** | Heap / Priority Queue |

## 1. Your Solution Assessment

```java
public int findKthLargest(int[] nums, int k) {
    PriorityQueue<Integer> minHeap = new PriorityQueue<>();

    for (int num : nums) {
        minHeap.offer(num);

        if (minHeap.size() > k) minHeap.poll();
    }

    return minHeap.peek();
}
```

**Correctness:** Correct. This is the classic "bounded min-heap" pattern: keep a min-heap
that never holds more than `k` elements. Every time the heap grows past `k`, the smallest
element is evicted, so the heap always contains the `k` largest elements seen so far. Once
the whole array has been processed, the root of the heap (the smallest of those `k`
elements) is exactly the `k`th largest element overall. It handles duplicates correctly
because duplicates occupy distinct heap slots — the problem explicitly wants the `k`th
largest *value* in sorted order, not the `k`th distinct value.

**Code quality:** Clean and idiomatic. The single-purpose loop and early-eviction check read
naturally, and the variable name `minHeap` documents the ordering choice. Nothing to change.

**Time complexity:** `O(n log k)`. Every element triggers one `offer` (`O(log k)`, since the
heap never exceeds size `k`); at most one `poll` follows, also `O(log k)`.

**Space complexity:** `O(k)` — the heap never holds more than `k` elements.

**Algorithm trace** (step table, input `nums = [3, 2, 1, 5, 6, 4]`, `k = 2`):

| num | heap after offer | size > k? | poll removes | heap after poll |
|---|---|---|---|---|
| 3 | {3} | No (1) | — | {3} |
| 2 | {2, 3} | No (2) | — | {2, 3} |
| 1 | {1, 2, 3} | Yes (3) | 1 | {2, 3} |
| 5 | {2, 3, 5} | Yes (3) | 2 | {3, 5} |
| 6 | {3, 5, 6} | Yes (3) | 3 | {5, 6} |
| 4 | {4, 5, 6} | Yes (3) | 4 | {5, 6} |

→ `peek()` = **5**

## 2. Optimal Approach

**Quickselect.** Reformulate "kth largest" as "the element that would sit at index
`n - k` if the array were sorted ascending." Partition the array around a pivot (Lomuto
scheme, using the last element as pivot) exactly like quicksort, but only recurse into the
side that contains the target index — the other side is discarded entirely, so on average
each partition throws away roughly half the remaining work.

- **Time complexity:** `O(n)` average — the work forms a geometric series (`n + n/2 + n/4 +
  ...`) that sums to `O(n)`. Worst case `O(n^2)` with an adversarial pivot choice (e.g.
  already-sorted input with last-element pivoting); this can be mitigated with a random
  pivot or median-of-medians for a guaranteed `O(n)`.
- **Space complexity:** `O(1)` extra (in-place partitioning) plus `O(log n)` average
  recursion stack (`O(n)` worst case).

```java
public int findKthLargest(int[] nums, int k) {
    int targetIndex = nums.length - k;
    return quickSelect(nums, 0, nums.length - 1, targetIndex);
}

private int quickSelect(int[] nums, int left, int right, int targetIndex) {
    int pivotIndex = partition(nums, left, right);

    if (pivotIndex == targetIndex) {
        return nums[pivotIndex];
    } else if (pivotIndex < targetIndex) {
        return quickSelect(nums, pivotIndex + 1, right, targetIndex);
    } else {
        return quickSelect(nums, left, pivotIndex - 1, targetIndex);
    }
}

private int partition(int[] nums, int left, int right) {
    int pivot = nums[right];
    int i = left;

    for (int j = left; j < right; j++) {
        if (nums[j] <= pivot) {
            swap(nums, i, j);
            i++;
        }
    }

    swap(nums, i, right);
    return i;
}

private void swap(int[] nums, int i, int j) {
    int temp = nums[i];
    nums[i] = nums[j];
    nums[j] = temp;
}
```

**Algorithm trace** (call stack table, input `nums = [3, 2, 1, 5, 6, 4]`, `k = 2`, so
`targetIndex = 4`):

| Depth | Call (left, right) | Pivot | Partition result | Action |
|---|---|---|---|---|
| 0 | quickSelect(0, 5) | nums[5]=4 | pivotIndex=3, array=[3,2,1,4,6,5] | 3 < 4 → recurse right (4, 5) |
| 1 | quickSelect(4, 5) | nums[5]=5 | pivotIndex=4, array=[3,2,1,4,5,6] | 4 == 4 → return nums[4] |

→ return **5**

## 3. Alternative Approaches

### a) Full sort

Sort the array and index directly into it.

```java
public int findKthLargest(int[] nums, int k) {
    Arrays.sort(nums);
    return nums[nums.length - k];
}
```

- **Time complexity:** `O(n log n)` — dominated by the sort.
- **Space complexity:** `O(log n)` to `O(n)`, depending on the JDK's dual-pivot quicksort
  recursion depth for primitive arrays.
- **When acceptable:** Almost always the fastest to write correctly under interview time
  pressure, and fine unless the interviewer specifically asks you to beat `O(n log n)` (as
  this problem's "Can you solve it without sorting?" prompt does).

**Algorithm trace** (annotated array, input `nums = [3, 2, 1, 5, 6, 4]`, `k = 2`):

```
Sorted ascending: [1, 2, 3, 4, 5, 6]
index = nums.length - k = 4
[1, 2, 3, 4, 5, 6]
             ^
          index=4 → value 5
```

→ return **5**

### b) Max-heap of all elements

Push every element into a max-heap, then pop `k - 1` times and peek.

```java
public int findKthLargest(int[] nums, int k) {
    PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());

    for (int num : nums) {
        maxHeap.offer(num);
    }

    for (int i = 0; i < k - 1; i++) {
        maxHeap.poll();
    }

    return maxHeap.peek();
}
```

- **Time complexity:** `O(n log n + k log n)` — building the heap via repeated `offer` calls
  is `O(n log n)`; each of the `k - 1` pops is `O(log n)`. (A true `O(n)` heapify is possible
  via `PriorityQueue`'s collection constructor, but is awkward with a primitive `int[]`.)
- **Space complexity:** `O(n)` — the heap holds every element.
- **When acceptable:** Simple to write, and reasonable when `k` is close to `n` (few pops
  needed) — but strictly worse than the bounded min-heap for small `k`, since it pays `O(n)`
  space and a full `O(n log n)` build regardless of `k`.

**Algorithm trace** (step table, input `nums = [3, 2, 1, 5, 6, 4]`, `k = 2`):

| Step | Action | Heap top |
|---|---|---|
| Build | Insert all 6 elements | 6 |
| Pop 1 (k−1=1) | Remove max (6) | 5 |

→ `peek()` = **5**

### c) Brute-force repeated max scan

Scan the array `k` times, each time picking the largest remaining (not-yet-picked) element.

```java
public int findKthLargest(int[] nums, int k) {
    boolean[] removed = new boolean[nums.length];
    int result = 0;

    for (int i = 0; i < k; i++) {
        int maxIndex = -1;

        for (int j = 0; j < nums.length; j++) {
            if (!removed[j] && (maxIndex == -1 || nums[j] > nums[maxIndex])) {
                maxIndex = j;
            }
        }

        removed[maxIndex] = true;
        result = nums[maxIndex];
    }

    return result;
}
```

- **Time complexity:** `O(n * k)` — `k` linear scans over the array.
- **Space complexity:** `O(n)` for the `removed` marker array (`O(1)` if you mutate `nums`
  in place instead, e.g. by overwriting picked elements with `Integer.MIN_VALUE`).
- **When acceptable:** Only for tiny inputs or very small, fixed `k` — it's the natural
  "obvious" first approach to state out loud before optimizing in an interview.

**Algorithm trace** (step table, input `nums = [3, 2, 1, 5, 6, 4]`, `k = 2`):

| Pass | Scan (excluding removed) | Max found | Index | result |
|---|---|---|---|---|
| 1 | [3,2,1,5,6,4] | 6 | 4 | 6 |
| 2 | [3,2,1,5,_,4] | 5 | 3 | 5 |

→ return **5**

---

## Bonus: Kth Largest *Distinct* Element

A natural follow-up: what if `k` refers to rank among **distinct** values instead of sorted
order (e.g. `nums = [2, 2, 3, 3, 4]`, `k = 2` → distinct values sorted descending are
`[4, 3, 2]`, so the answer is `3`, not `3` from position 2 of the raw sorted array — those
happen to coincide here, but `k = 3` would give `2`, whereas the non-distinct version would
give `3`)?

Your instinct is right: the straightforward fix is a bounded min-heap **plus** a `HashSet`
to skip values you've already pushed. But that visited set can be avoided — its job (reject
duplicates) can be folded into a data structure that already guarantees uniqueness, or
eliminated entirely by sorting first.

### a) Bounded `TreeSet` (no separate visited set)

A `TreeSet` is a sorted, deduplicating collection (backed by a red-black tree): `add()` is a
silent no-op if the value is already present. That means the exact "bounded to size `k`"
trick from the original problem works unmodified — you just swap `PriorityQueue` for
`TreeSet`, and duplicate-skipping falls out of the data structure's own contract instead of
needing a second `HashSet` alongside it.

```java
public int findKthLargestDistinct(int[] nums, int k) {
    TreeSet<Integer> set = new TreeSet<>();

    for (int num : nums) {
        set.add(num);

        if (set.size() > k) {
            set.pollFirst();
        }
    }

    return set.first();
}
```

- **Time complexity:** `O(n log k)` — same shape as the original bounded min-heap; `add` and
  `pollFirst` on a red-black tree capped at `k` elements are both `O(log k)`.
- **Space complexity:** `O(k)`.

**Algorithm trace** (step table, input `nums = [2, 2, 3, 3, 4]`, `k = 2`):

| num | set before | add result | set after | size > k? | pollFirst |
|---|---|---|---|---|---|
| 2 | {} | added | {2} | No (1) | — |
| 2 | {2} | no-op (dup) | {2} | No (1) | — |
| 3 | {2} | added | {2, 3} | No (2) | — |
| 3 | {2, 3} | no-op (dup) | {2, 3} | No (2) | — |
| 4 | {2, 3} | added | {2, 3, 4} | Yes (3) | removes 2 → {3, 4} |

→ `set.first()` = **3**

### b) Sort, then skip adjacent duplicates (no set at all)

Sorting brings equal values next to each other, so uniqueness no longer needs a hash
structure — comparing each element to the single previous distinct value is enough.

```java
public int findKthLargestDistinct(int[] nums, int k) {
    Arrays.sort(nums);

    int distinctCount = 0;
    Integer prev = null;

    for (int i = nums.length - 1; i >= 0; i--) {
        if (prev == null || nums[i] != prev) {
            distinctCount++;
            prev = nums[i];

            if (distinctCount == k) {
                return nums[i];
            }
        }
    }

    throw new IllegalArgumentException("k exceeds number of distinct elements");
}
```

- **Time complexity:** `O(n log n)` for the sort, `O(n)` for the scan → `O(n log n)`
  overall.
- **Space complexity:** `O(1)` extra (beyond the sort's own recursion stack).

**Algorithm trace** (step table, input `nums = [2, 2, 3, 3, 4]`, `k = 2`, sorted ascending
`[2, 2, 3, 3, 4]`, scanned from the end):

| i | nums[i] | prev | distinct? | distinctCount | k reached? |
|---|---|---|---|---|---|
| 4 | 4 | none | Yes | 1 | No |
| 3 | 3 | 4 | Yes (3≠4) | 2 | **Yes → return 3** |

→ return **3**

### Why the `TreeSet` version is the better answer

Both avoid a *separate* `HashSet`, but they trade off differently: the sort-based version
is simpler and needs no auxiliary structure at all, but pays `O(n log n)` unconditionally —
same as full-sort for the original problem. The bounded `TreeSet`, like the bounded
min-heap it replaces, only ever holds `k` elements and matches the original problem's
`O(n log k)` bound, while still getting deduplication "for free" from the data structure's
own invariant instead of an explicit visited set. If `k` is small relative to `n` (the case
this pattern is usually chosen for), the `TreeSet` approach is the direct analogue of your
original solution and the one worth reaching for first.

Quickselect doesn't extend as cleanly here: its index arithmetic assumes every array
position is significant, but distinct-rank selection needs positions collapsed first —
so you'd dedupe (via sort or a set) before quickselecting on the reduced array, which
reintroduces the very step this section is about avoiding.
