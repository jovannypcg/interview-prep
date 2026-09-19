# Longest Substring Without Repeating Characters

**Date added:** 2026-09-19

## Problem Description

Given a string `s`, find the length of the longest substring without duplicate characters.

**Source:** https://leetcode.com/problems/longest-substring-without-repeating-characters/

## Examples

**Example 1**
```
Input: s = "abcabcbb"
Output: 3
Explanation: The answer is "abc", with the length of 3. Note that "bca" and "cab" are also correct answers.
```

```mermaid
graph LR
    classDef inWindow fill:#4CAF50,stroke:#2E7D32,color:#fff,stroke-width:2px
    classDef dup fill:#F44336,stroke:#B71C1C,color:#fff,stroke-width:2px
    classDef outside fill:#ECEFF1,stroke:#90A4AE,color:#37474F

    A0["a<br/>0"]:::inWindow --> B1["b<br/>1"]:::inWindow --> C2["c<br/>2"]:::inWindow --> D3["a<br/>3<br/>dup of 0"]:::dup --> E4["b<br/>4"]:::outside --> F5["c<br/>5"]:::outside --> G6["b<br/>6"]:::outside --> H7["b<br/>7"]:::outside
```

**Example 2**
```
Input: s = "bbbbb"
Output: 1
Explanation: The answer is "b", with the length of 1.
```

```mermaid
graph LR
    classDef inWindow fill:#4CAF50,stroke:#2E7D32,color:#fff,stroke-width:2px
    classDef dup fill:#F44336,stroke:#B71C1C,color:#fff,stroke-width:2px

    A0["b<br/>0"]:::inWindow --> B1["b<br/>1<br/>dup of 0"]:::dup --> C2["b<br/>2<br/>dup of 1"]:::dup --> D3["b<br/>3<br/>dup of 2"]:::dup --> E4["b<br/>4<br/>dup of 3"]:::dup
```

**Example 3**
```
Input: s = "pwwkew"
Output: 3
Explanation: The answer is "wke", with the length of 3.
Notice that the answer must be a substring, "pwke" is a subsequence and not a substring.
```

```mermaid
graph LR
    classDef inWindow fill:#4CAF50,stroke:#2E7D32,color:#fff,stroke-width:2px
    classDef dup fill:#F44336,stroke:#B71C1C,color:#fff,stroke-width:2px
    classDef outside fill:#ECEFF1,stroke:#90A4AE,color:#37474F

    A0["p<br/>0"]:::outside --> B1["w<br/>1"]:::outside --> C2["w<br/>2<br/>dup of 1"]:::dup --> D3["k<br/>3"]:::inWindow --> E4["e<br/>4"]:::inWindow --> F5["w<br/>5<br/>dup of 2"]:::dup

    C2 -.->|"window resets to start at 2"| D3
```

**Example 4**
```
Input: s = ""
Output: 0
Explanation: The empty string has no characters, so the longest substring without repeating characters has length 0.
```

```mermaid
graph LR
    classDef empty fill:#90A4AE,stroke:#37474F,color:#fff,stroke-width:2px

    A["∅ empty string<br/>length = 0"]:::empty
```

**Example 5**
```
Input: s = " "
Output: 1
Explanation: A single space is one character; a substring of length 1 trivially has no duplicates.
```

```mermaid
graph LR
    classDef inWindow fill:#4CAF50,stroke:#2E7D32,color:#fff,stroke-width:2px

    A0["' '<br/>0"]:::inWindow
```

**Example 6**
```
Input: s = "dvdf"
Output: 3
Explanation: The answer is "vdf", with the length of 3. Notice that the 'd' at index 2 repeats
the 'd' at index 0, but index 0 has already fallen outside the current window, so it does not
force a shrink — this is the classic trap in this problem.
```

```mermaid
graph LR
    classDef inWindow fill:#4CAF50,stroke:#2E7D32,color:#fff,stroke-width:2px
    classDef outside fill:#ECEFF1,stroke:#90A4AE,color:#37474F

    A0["d<br/>0"]:::outside --> B1["v<br/>1"]:::inWindow --> C2["d<br/>2"]:::inWindow --> D3["f<br/>3"]:::inWindow

    A0 -.->|"same char, but already outside the window"| C2
```

**Example 7**
```
Input: s = "abba"
Output: 2
Explanation: Both "ab" (indices 0-1) and "ba" (indices 2-3) are valid answers of length 2. The
'a' at index 3 repeats the 'a' at index 0, but index 0 is already outside the window by the time
we reach index 3 — a naive implementation that blindly resets the left pointer to the stale index
would compute the wrong answer here.
```

```mermaid
graph LR
    classDef windowOne fill:#2196F3,stroke:#0D47A1,color:#fff,stroke-width:2px
    classDef dup fill:#F44336,stroke:#B71C1C,color:#fff,stroke-width:2px
    classDef windowTwo fill:#4CAF50,stroke:#2E7D32,color:#fff,stroke-width:2px

    A0["a<br/>0"]:::windowOne --> B1["b<br/>1"]:::windowOne --> C2["b<br/>2<br/>dup of 1"]:::dup --> D3["a<br/>3"]:::windowTwo

    A0 -.->|"same char, but already outside the window"| D3
```

## Constraints

- `0 <= s.length <= 10^5`
- `s` consists of English letters, digits, symbols and spaces.

## Hints

1. A brute-force approach would check every substring for duplicates — what is its time complexity, and can work be reused as a window slides instead of restarting each check?
2. Think of a window `[left, right)` that expands one character at a time. What should happen when the incoming character already exists inside the current window?
3. A hash map (or a fixed-size array, since characters are bounded) can store the *last seen index* of each character for O(1) lookup.
4. When you find a duplicate, don't restart `left` from scratch — only move it past the previous occurrence, and **only if that occurrence is still inside the current window** (see the `"dvdf"` and `"abba"` examples above for why this matters).
5. Track the maximum window size (`right - left + 1`) as you go, updating it after every expansion.
