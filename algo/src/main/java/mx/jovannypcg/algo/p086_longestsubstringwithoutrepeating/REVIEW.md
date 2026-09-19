| | |
|---|---|
| **Solved on** | 2026-09-19 |
| **DSA Category** | Sliding Window |

## 1. Your Solution Assessment

**Correctness:** Correct. Hand-traced against all 7 README examples plus every boundary case in
`SolutionTest.java` (empty string, single space, digits/symbols, `"dvdf"`, `"abba"`, and the
10^5-length cycling input) — all pass, including the two classic traps this problem sets:

- `"dvdf"`: the second `d` (index 2) repeats the first `d` (index 0), but index 0 already fell
  out of the window by the time index 2 is reached, so it correctly does **not** trigger a shrink.
- `"abba"`: the trailing `a` (index 3) repeats the leading `a` (index 0), which is also already
  outside the window — handled correctly because the shrink loop only evicts characters while
  they're actually still `seen`, rather than jumping to a stored index that might be stale.

**Code quality:** Clear variable names (`left`, `right`, `longest`, `seen`), an early return for
the empty-string edge case, and a `continue` that keeps the "grow" and "shrink" branches visually
separate. The `while (seen.contains(current)) { seen.remove(str.charAt(left++)); }` line is doing
two jobs at once (evict + advance `left`) — worth knowing that pattern by name since it's what
gives this approach its correctness without needing a "last seen index" map.

**Time complexity:** O(n), where n = `str.length()`. It looks like a nested loop (`while` inside
`while`), but `left` and `right` each only ever move forward and each visits every index at most
once across the *entire* run — so the two loops together do O(n) total work, not O(n²).

**Space complexity:** O(min(n, k)), where k is the size of the character set — `seen` holds at
most one entry per distinct character in the current window.

**Algorithm trace** (annotated array, showing only the pivotal duplicate/evict events):

Input: `s = "abcabcbb"`

```
Grow to first duplicate:
[a, b, c, a, b, c, b, b]
 L        R
window = "abc" (len 3) → duplicate 'a' found, record longest=3, evict 'a'

[a, b, c, a, b, c, b, b]
    L     R
window = "bca" (len 3) → duplicate 'b' found, record longest=3, evict 'b'

[a, b, c, a, b, c, b, b]
       L     R
window = "cab" (len 3) → duplicate 'c' found, record longest=3, evict 'c'

[a, b, c, a, b, c, b, b]
          L     R
window = "abc" (len 3) → duplicate 'b' found, record longest=3, evict 'a' then 'b' (2 evictions)

[a, b, c, a, b, c, b, b]
                L  R
window = "cb" (len 2) → duplicate 'b' found, record longest=3, evict 'c' then 'b' (2 evictions)

[a, b, c, a, b, c, b, b]
                   L R
window = "b" (len 1), loop ends → final longest = max(3, 1) = 3
```
→ return `3`

## 2. Optimal Approach

The canonical optimal solution swaps the `Set<Character>` + shrink-loop for a
`Map<Character, Integer>` that stores each character's **last seen index**. Instead of evicting
characters one at a time from the left, `left` jumps directly past the previous occurrence in a
single step — same asymptotic complexity, but no inner loop at all, which makes the single-pass
nature of the algorithm explicit in the code.

**Time complexity:** O(n) — one pass, one map lookup/update per character.

**Space complexity:** O(min(n, k)) — the map holds at most one entry per distinct character.

```java
public int lengthOfLongestSubstring(String s) {
    Map<Character, Integer> lastSeen = new HashMap<>();
    int left = 0, longest = 0;

    for (int right = 0; right < s.length(); right++) {
        char c = s.charAt(right);

        if (lastSeen.containsKey(c) && lastSeen.get(c) >= left) {
            left = lastSeen.get(c) + 1;
        }

        lastSeen.put(c, right);
        longest = Math.max(longest, right - left + 1);
    }

    return longest;
}
```

**Algorithm trace** (annotated array):

Input: `s = "abcabcbb"`

```
[a, b, c, a, b, c, b, b]
          L
          R
right=3 'a': lastSeen[a]=0 >= left(0) → left jumps to 1. window="bca" len3, longest=3

[a, b, c, a, b, c, b, b]
             L  R
right=4 'b': lastSeen[b]=1 >= left(1) → left jumps to 2. window="cab" len3, longest=3

[a, b, c, a, b, c, b, b]
                L  R
right=5 'c': lastSeen[c]=2 >= left(2) → left jumps to 3. window="abc" len3, longest=3

[a, b, c, a, b, c, b, b]
                   L  R
right=6 'b': lastSeen[b]=4 >= left(3) → left jumps to 5. window="cb" len2, longest=3

[a, b, c, a, b, c, b, b]
                      L R
right=7 'b': lastSeen[b]=6 >= left(5) → left jumps to 7. window="b" len1, longest=3
```
→ return `3`

Notice `left` jumps straight from 3→5 and 5→7 in one step, where the set-based shrink loop needed
two evictions to get there — same answer, fewer operations per step.

## 3. Alternative Approaches

### Brute force: check every substring

For every start index `i`, extend `j` and re-scan `s[i..j]` from scratch on each extension to
check for duplicates.

**Time complexity:** O(n³) — O(n²) substrings, O(n) to verify each one is duplicate-free.

**Space complexity:** O(min(n, k)) for the per-check duplicate set.

**When acceptable:** Only for very small inputs or as a first-pass "get something working"
answer under interview time pressure — it will time out well before `n = 10^5`.

**Algorithm trace** (annotated array):

Input: `s = "pwwkew"`

```
i=0: check "p" ✓, "pw" ✓, "pww" ✗ (duplicate w) → best from i=0 is "pw" (len 2)
[p, w, w, k, e, w]
 i     j
 (stops extending at j=2, where s[2]='w' duplicates s[1]='w')

i=1: check "w" ✓, "ww" ✗ → best from i=1 is "w" (len 1)
[p, w, w, k, e, w]
    i  j

i=2: check "w" ✓, "wk" ✓, "wke" ✓, "wkew" ✗ (duplicate w) → best from i=2 is "wke" (len 3)
[p, w, w, k, e, w]
       i        j
```
→ longest across all `i` = `3`

### Brute force with a running set per start index

Same idea, but instead of re-scanning from scratch for every `j`, keep a `Set<Character>` that
grows with `j` and reset it only when `i` advances.

**Time complexity:** O(n²) — for each of the n start indices, `j` scans forward up to n times.

**Space complexity:** O(min(n, k)) for the running set.

**When acceptable:** A reasonable middle-ground answer under interview pressure — correct and
easier to reason about than the sliding-window version, and still fast enough for moderate inputs
(low 1000s of characters).

**Algorithm trace** (annotated array):

Input: `s = "pwwkew"`

```
i=0: j grows 0→1 ("p","pw" ✓), stops at j=2 ('w' already in set) → len 2
[p, w, w, k, e, w]
 i  j

i=1: j grows to 1 only ('w' repeats immediately at j=2) → len 1
[p, w, w, k, e, w]
    i

i=2: j grows 2→4 ("w","wk","wke" ✓), stops at j=5 ('w' already in set) → len 3
[p, w, w, k, e, w]
       i        j
```
→ longest across all `i` = `3`
