# Add Two Numbers

**Date added:** 2026-09-21

## Problem Description

You are given two non-empty linked lists representing two non-negative integers. The digits
are stored in reverse order, and each of their nodes contains a single digit. Add the two
numbers and return the sum as a linked list.

You may assume the two numbers do not contain any leading zero, except the number 0 itself.

**Source:** https://leetcode.com/problems/add-two-numbers/

## Examples

**Example 1**
```
Input: l1 = [2,4,3], l2 = [5,6,4]
Output: [7,0,8]
Explanation: 342 + 465 = 807.
```
```mermaid
flowchart LR
    subgraph L1["l1 = 342"]
        direction LR
        A1((2)) --> A2((4)) --> A3((3))
    end
    subgraph L2["l2 = 465"]
        direction LR
        B1((5)) --> B2((6)) --> B3((4))
    end
    subgraph OUT["Output = 807"]
        direction LR
        C1((7)) --> C2((0)) --> C3((8))
    end

    classDef l1style fill:#4C9AFF,stroke:#0747A6,color:#fff;
    classDef l2style fill:#FF8B00,stroke:#B25000,color:#fff;
    classDef outstyle fill:#36B37E,stroke:#00875A,color:#fff;

    class A1,A2,A3 l1style
    class B1,B2,B3 l2style
    class C1,C2,C3 outstyle
```

**Example 2**
```
Input: l1 = [0], l2 = [0]
Output: [0]
Explanation: 0 + 0 = 0.
```
```mermaid
flowchart LR
    subgraph L1["l1 = 0"]
        A1((0))
    end
    subgraph L2["l2 = 0"]
        B1((0))
    end
    subgraph OUT["Output = 0"]
        C1((0))
    end

    classDef l1style fill:#4C9AFF,stroke:#0747A6,color:#fff;
    classDef l2style fill:#FF8B00,stroke:#B25000,color:#fff;
    classDef outstyle fill:#36B37E,stroke:#00875A,color:#fff;

    class A1 l1style
    class B1 l2style
    class C1 outstyle
```

**Example 3**
```
Input: l1 = [9,9,9,9,9,9,9], l2 = [9,9,9,9]
Output: [8,9,9,9,0,0,0,1]
Explanation: 9999999 + 9999 = 10009998, stored in reverse order.
```
```mermaid
flowchart LR
    subgraph L1["l1 = 9999999"]
        direction LR
        A1((9)) --> A2((9)) --> A3((9)) --> A4((9)) --> A5((9)) --> A6((9)) --> A7((9))
    end
    subgraph L2["l2 = 9999"]
        direction LR
        B1((9)) --> B2((9)) --> B3((9)) --> B4((9))
    end
    subgraph OUT["Output = 10009998"]
        direction LR
        C1((8)) --> C2((9)) --> C3((9)) --> C4((9)) --> C5((0)) --> C6((0)) --> C7((0)) --> C8((1))
    end

    classDef l1style fill:#4C9AFF,stroke:#0747A6,color:#fff;
    classDef l2style fill:#FF8B00,stroke:#B25000,color:#fff;
    classDef outstyle fill:#36B37E,stroke:#00875A,color:#fff;

    class A1,A2,A3,A4,A5,A6,A7 l1style
    class B1,B2,B3,B4 l2style
    class C1,C2,C3,C4,C5,C6,C7,C8 outstyle
```

**Example 4**
```
Input: l1 = [5], l2 = [5]
Output: [0,1]
Explanation: 5 + 5 = 10, which needs a carry into a new node.
```
```mermaid
flowchart LR
    subgraph L1["l1 = 5"]
        A1((5))
    end
    subgraph L2["l2 = 5"]
        B1((5))
    end
    subgraph OUT["Output = 10"]
        direction LR
        C1((0)) --> C2((1))
    end

    classDef l1style fill:#4C9AFF,stroke:#0747A6,color:#fff;
    classDef l2style fill:#FF8B00,stroke:#B25000,color:#fff;
    classDef outstyle fill:#36B37E,stroke:#00875A,color:#fff;

    class A1 l1style
    class B1 l2style
    class C1,C2 outstyle
```

**Example 5**
```
Input: l1 = [1,8], l2 = [0]
Output: [1,8]
Explanation: 81 + 0 = 81. The shorter list (l2) is exhausted immediately but l1 still has digits left.
```
```mermaid
flowchart LR
    subgraph L1["l1 = 81"]
        direction LR
        A1((1)) --> A2((8))
    end
    subgraph L2["l2 = 0"]
        B1((0))
    end
    subgraph OUT["Output = 81"]
        direction LR
        C1((1)) --> C2((8))
    end

    classDef l1style fill:#4C9AFF,stroke:#0747A6,color:#fff;
    classDef l2style fill:#FF8B00,stroke:#B25000,color:#fff;
    classDef outstyle fill:#36B37E,stroke:#00875A,color:#fff;

    class A1,A2 l1style
    class B1 l2style
    class C1,C2 outstyle
```

**Example 6**
```
Input: l1 = [2,4,9], l2 = [5,6,4,9]
Output: [7,0,4,0,1]
Explanation: 942 + 9465 = 10407. l2 is longer than l1, so addition continues after l1 is exhausted, plus a final carry.
```
```mermaid
flowchart LR
    subgraph L1["l1 = 942"]
        direction LR
        A1((2)) --> A2((4)) --> A3((9))
    end
    subgraph L2["l2 = 9465"]
        direction LR
        B1((5)) --> B2((6)) --> B3((4)) --> B4((9))
    end
    subgraph OUT["Output = 10407"]
        direction LR
        C1((7)) --> C2((0)) --> C3((4)) --> C4((0)) --> C5((1))
    end

    classDef l1style fill:#4C9AFF,stroke:#0747A6,color:#fff;
    classDef l2style fill:#FF8B00,stroke:#B25000,color:#fff;
    classDef outstyle fill:#36B37E,stroke:#00875A,color:#fff;

    class A1,A2,A3 l1style
    class B1,B2,B3,B4 l2style
    class C1,C2,C3,C4,C5 outstyle
```

**Example 7**
```
Input: l1 = [9,9], l2 = [1]
Output: [0,0,1]
Explanation: 99 + 1 = 100. Multiple consecutive carries ripple all the way through and produce an extra node.
```
```mermaid
flowchart LR
    subgraph L1["l1 = 99"]
        direction LR
        A1((9)) --> A2((9))
    end
    subgraph L2["l2 = 1"]
        B1((1))
    end
    subgraph OUT["Output = 100"]
        direction LR
        C1((0)) --> C2((0)) --> C3((1))
    end

    classDef l1style fill:#4C9AFF,stroke:#0747A6,color:#fff;
    classDef l2style fill:#FF8B00,stroke:#B25000,color:#fff;
    classDef outstyle fill:#36B37E,stroke:#00875A,color:#fff;

    class A1,A2 l1style
    class B1 l2style
    class C1,C2,C3 outstyle
```

## Constraints

- The number of nodes in each linked list is in the range `[1, 100]`.
- `0 <= Node.val <= 9`
- It is guaranteed that the list represents a number that does not have leading zeros.

## Hints

1. Think about how you'd add two numbers by hand, digit by digit, starting from the ones place — which end of each list does that correspond to?
2. Since digits are stored in reverse order, you can walk both lists simultaneously from their heads without needing to reverse anything first.
3. Keep track of a running `carry` value as you add corresponding digits together.
4. The two lists may have different lengths — what should happen when one list runs out of nodes before the other?
5. Don't forget the case where a carry remains after both lists are fully consumed; it needs to become one final extra node.
