# Remove Nth Node From End of List

**Date added:** 2026-06-30

## Problem Description

Given the head of a linked list, remove the nth node from the end of the list and return its head.

**Source:** https://leetcode.com/problems/remove-nth-node-from-end-of-list/

## Examples

**Example 1 — interior node**
```
Input: head = [1,2,3,4,5], n = 2
Output: [1,2,3,5]
Explanation: The 2nd node from the end is 4 (at index 3). Removing it yields [1,2,3,5].
```
```mermaid
flowchart LR
    subgraph Before
        direction LR
        a1((1)) --> a2((2)) --> a3((3)) --> a4((4)) --> a5((5)) --> an[null]
    end
    subgraph After
        direction LR
        b1((1)) --> b2((2)) --> b3((3)) --> b5((5)) --> bn[null]
    end
    Before ~~~ After
    classDef kept fill:#4dabf7,stroke:#1971c2,color:#fff
    classDef removed fill:#ff6b6b,stroke:#c92a2a,color:#fff,stroke-width:2px
    class a1,a2,a3,a5,b1,b2,b3,b5 kept
    class a4 removed
```

**Example 2 — single node, list becomes empty**
```
Input: head = [1], n = 1
Output: []
Explanation: The only node is also the 1st from the end. Removing it yields an empty list.
```
```mermaid
flowchart LR
    subgraph Before
        direction LR
        a1((1)) --> an[null]
    end
    subgraph After
        direction LR
        bn[null]
    end
    Before ~~~ After
    classDef removed fill:#ff6b6b,stroke:#c92a2a,color:#fff,stroke-width:2px
    class a1 removed
```

**Example 3 — remove the tail**
```
Input: head = [1,2], n = 1
Output: [1]
Explanation: The 1st node from the end is 2. Removing it yields [1].
```
```mermaid
flowchart LR
    subgraph Before
        direction LR
        a1((1)) --> a2((2)) --> an[null]
    end
    subgraph After
        direction LR
        b1((1)) --> bn[null]
    end
    Before ~~~ After
    classDef kept fill:#4dabf7,stroke:#1971c2,color:#fff
    classDef removed fill:#ff6b6b,stroke:#c92a2a,color:#fff,stroke-width:2px
    class a1,b1 kept
    class a2 removed
```

**Example 4 — remove the head, new head emerges**
```
Input: head = [1,2], n = 2
Output: [2]
Explanation: The 2nd node from the end is 1 (the head). Removing it yields [2].
```
```mermaid
flowchart LR
    subgraph Before
        direction LR
        a1((1)) --> a2((2)) --> an[null]
    end
    subgraph After
        direction LR
        b2((2)) --> bn[null]
    end
    Before ~~~ After
    classDef removed fill:#ff6b6b,stroke:#c92a2a,color:#fff,stroke-width:2px
    classDef newhead fill:#51cf66,stroke:#2f9e44,color:#fff,stroke-width:2px
    class a1 removed
    class a2,b2 newhead
```

**Example 5 — odd-length list, exact middle**
```
Input: head = [1,2,3], n = 2
Output: [1,3]
Explanation: The 2nd node from the end is 2. Removing it yields [1,3].
```
```mermaid
flowchart LR
    subgraph Before
        direction LR
        a1((1)) --> a2((2)) --> a3((3)) --> an[null]
    end
    subgraph After
        direction LR
        b1((1)) --> b3((3)) --> bn[null]
    end
    Before ~~~ After
    classDef kept fill:#4dabf7,stroke:#1971c2,color:#fff
    classDef removed fill:#ff6b6b,stroke:#c92a2a,color:#fff,stroke-width:2px
    class a1,a3,b1,b3 kept
    class a2 removed
```

**Example 6 — larger list, head removed again**
```
Input: head = [1,2,3,4,5,6,7], n = 7
Output: [2,3,4,5,6,7]
Explanation: n equals the list size, so the 7th node from the end is the head (1). Removing it yields [2,3,4,5,6,7].
```
```mermaid
flowchart LR
    subgraph Before
        direction LR
        a1((1)) --> a2((2)) --> a3((3)) --> a4((4)) --> a5((5)) --> a6((6)) --> a7((7)) --> an[null]
    end
    subgraph After
        direction LR
        b2((2)) --> b3((3)) --> b4((4)) --> b5((5)) --> b6((6)) --> b7((7)) --> bn[null]
    end
    Before ~~~ After
    classDef kept fill:#4dabf7,stroke:#1971c2,color:#fff
    classDef removed fill:#ff6b6b,stroke:#c92a2a,color:#fff,stroke-width:2px
    classDef newhead fill:#51cf66,stroke:#2f9e44,color:#fff,stroke-width:2px
    class a3,a4,a5,a6,a7,b3,b4,b5,b6,b7 kept
    class a1 removed
    class a2,b2 newhead
```

**Example 7 — duplicate values, position matters**
```
Input: head = [7,7,7,7], n = 2
Output: [7,7,7]
Explanation: With sz = 4 and n = 2, the node to remove is at position (sz - n + 1) = 3 from the front.
Even though every value is 7, it's the 3rd node specifically that's unlinked, leaving three 7s: [7,7,7].
```
```mermaid
flowchart LR
    subgraph Before
        direction LR
        a1(("7₁")) --> a2(("7₂")) --> a3(("7₃")) --> a4(("7₄")) --> an[null]
    end
    subgraph After
        direction LR
        b1(("7₁")) --> b2(("7₂")) --> b4(("7₄")) --> bn[null]
    end
    Before ~~~ After
    classDef kept fill:#4dabf7,stroke:#1971c2,color:#fff
    classDef removed fill:#ff6b6b,stroke:#c92a2a,color:#fff,stroke-width:2px
    class a1,a2,a4,b1,b2,b4 kept
    class a3 removed
```

## Constraints

- The number of nodes in the list is `sz`.
- `1 <= sz <= 30`
- `0 <= Node.val <= 100`
- `1 <= n <= sz`

## Hints

1. If you knew the total length of the list, could you figure out which node (from the front) to remove?
2. Can you find the node to remove in a single pass without computing the length first?
3. Think about using two pointers that maintain a fixed gap between them.
4. If the fast pointer starts `n` steps ahead of the slow pointer, where are they relative to each other when fast reaches the end?
5. You need a reference to the node *before* the one you want to remove — how does that change where you start your slow pointer?
