# Linked List Cycle Detection

**Date added:** 2026-09-19

## Problem Description

Given the beginning of a linked list `head`, return `true` if there is a cycle in the linked
list. Otherwise, return `false`.

There is a cycle in a linked list if at least one node in the list can be visited again by
following the `next` pointer.

Internally, `index` determines the index of the beginning of the cycle, if it exists. The tail
node of the list will set its `next` pointer to the `index`-th node. If `index = -1`, then the
tail node points to `null` and no cycle exists.

**Note:** `index` is not given to you as a parameter — it only describes how the test input is
constructed.

**Source:** https://neetcode.io/problems/linked-list-cycle-detection

## Examples

**Example 1**
```
Input: head = [3,2,0,-4], index = 1
Output: true
Explanation: The tail node (-4) connects back to the 1st node (0-indexed), which holds value 2.
```

```mermaid
graph LR
    N0(("3<br/>head")) --> N1(("2"))
    N1 --> N2(("0"))
    N2 --> N3(("-4"))
    N3 -.->|"back-edge"| N1

    classDef head fill:#7fd3ff,stroke:#0366a6,stroke-width:2px,color:#003049
    classDef cycle fill:#ffd166,stroke:#b8860b,stroke-width:2px,color:#4a3200

    class N0 head
    class N1,N2,N3 cycle
    linkStyle 3 stroke:#ff4d4d,stroke-width:3px,stroke-dasharray:5 5
```

**Example 2**
```
Input: head = [1,2], index = -1
Output: false
Explanation: The tail node points to null, so there is no cycle.
```

```mermaid
graph LR
    N0(("1<br/>head")) --> N1(("2"))
    N1 --> Z["null"]

    classDef head fill:#7fd3ff,stroke:#0366a6,stroke-width:2px,color:#003049
    classDef plain fill:#b7f7c1,stroke:#2e8b57,stroke-width:2px,color:#123d1f
    classDef term fill:#e0e0e0,stroke:#777,stroke-width:1px,color:#333

    class N0 head
    class N1 plain
    class Z term
```

**Example 3**
```
Input: head = [1], index = -1
Output: false
Explanation: A single node with no cycle points directly to null.
```

```mermaid
graph LR
    N0(("1<br/>head")) --> Z["null"]

    classDef head fill:#7fd3ff,stroke:#0366a6,stroke-width:2px,color:#003049
    classDef term fill:#e0e0e0,stroke:#777,stroke-width:1px,color:#333

    class N0 head
    class Z term
```

**Example 4**
```
Input: head = [1], index = 0
Output: true
Explanation: A single node whose next pointer points back to itself forms a self-loop.
```

```mermaid
graph LR
    N0(("1<br/>head")) -.->|"back-edge"| N0

    classDef cycle fill:#ffd166,stroke:#b8860b,stroke-width:2px,color:#4a3200

    class N0 cycle
    linkStyle 0 stroke:#ff4d4d,stroke-width:3px,stroke-dasharray:5 5
```

**Example 5**
```
Input: head = [], index = -1
Output: false
Explanation: An empty list has no nodes, so it trivially has no cycle.
```

```mermaid
graph LR
    Z["null<br/>(head)"]

    classDef term fill:#e0e0e0,stroke:#777,stroke-width:1px,color:#333

    class Z term
```

**Example 6**
```
Input: head = [1,2,3,4,5,6], index = 2
Output: true
Explanation: Nodes 1 and 2 form a non-cyclic "tail" leading into the cycle, which starts at the
3rd node (0-indexed) holding value 3 and loops through 4, 5, 6 before returning to it.
```

```mermaid
graph LR
    N0(("1<br/>head")) --> N1(("2"))
    N1 --> N2(("3"))
    N2 --> N3(("4"))
    N3 --> N4(("5"))
    N4 --> N5(("6"))
    N5 -.->|"back-edge"| N2

    classDef head fill:#7fd3ff,stroke:#0366a6,stroke-width:2px,color:#003049
    classDef plain fill:#b7f7c1,stroke:#2e8b57,stroke-width:2px,color:#123d1f
    classDef cycle fill:#ffd166,stroke:#b8860b,stroke-width:2px,color:#4a3200

    class N0 head
    class N1 plain
    class N2,N3,N4,N5 cycle
    linkStyle 5 stroke:#ff4d4d,stroke-width:3px,stroke-dasharray:5 5
```

**Example 7**
```
Input: head = [5,4,3,2,1], index = 0
Output: true
Explanation: The entire list is one big loop — the tail (1) connects back to the head (5), so
every node, including the head, is part of the cycle.
```

```mermaid
graph LR
    N0(("5<br/>head")) --> N1(("4"))
    N1 --> N2(("3"))
    N2 --> N3(("2"))
    N3 --> N4(("1"))
    N4 -.->|"back-edge"| N0

    classDef cycle fill:#ffd166,stroke:#b8860b,stroke-width:3px,color:#4a3200

    class N0,N1,N2,N3,N4 cycle
    linkStyle 4 stroke:#ff4d4d,stroke-width:3px,stroke-dasharray:5 5
```

## Constraints

- `0 <= Length of the list <= 1000`.
- `-1000 <= Node.val <= 1000`
- `index` is `-1` or a valid index in the linked list.

## Hints

1. Think about what "revisiting a node" means while you traverse — what would you need to
   remember about the nodes you've already seen?
2. A hash set of visited node references would work, but it costs O(n) extra space. Can you do
   it with O(1) space instead?
3. Consider using two pointers that traverse the list at different speeds.
4. If a cycle exists, a faster-moving pointer will eventually "lap" a slower one and they'll
   land on the exact same node.
5. If the faster pointer (or its `next`) ever reaches `null`, the list has no cycle.
