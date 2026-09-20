# Dummy Node

## Pattern
Introduce a placeholder node with no meaningful data — positioned right before the real head (or before the first node of a list you're building) — so the "head might change" case collapses into the general case. Removes the need for a separate `if (head == ...)` branch.

## How to Recognize
- The head of the list might need to be removed, replaced, or is unknown until the algorithm runs (e.g., "remove nth from end", "remove duplicates", "partition list")
- You're building a brand-new list node by node (merge, filter, copy) and don't want a null check before the first append
- A doubly linked list needs sentinels at *both* ends so insert/remove never has to check for "am I at the boundary?" (e.g., LRU Cache)
- You catch yourself writing near-duplicate logic for "first node" vs "every other node"

## Approach
- **Removal/modification in place**: `ListNode dummy = new ListNode(0, head);` Do all pointer surgery relative to `dummy`, then return `dummy.next` — automatically the new head, even if the original head was removed.
- **Building a new list**: `ListNode dummy = new ListNode(0);` plus a `tail` pointer starting at `dummy`. Always append via `tail.next = node; tail = tail.next;`, then return `dummy.next`.
- **Sentinel pair (doubly linked)**: keep a `head` sentinel and a `tail` sentinel permanently linked to each other. Every real node is inserted/removed between two existing nodes, so there's never a null-neighbor edge case.

## Template

```java
// Removal — dummy absorbs a possible head removal
public ListNode deleteDuplicates(ListNode head) {
    ListNode dummy = new ListNode(0, head);
    ListNode prev = dummy;

    while (prev.next != null && prev.next.next != null) {
        if (prev.next.val == prev.next.next.val) {
            int dup = prev.next.val;
            while (prev.next != null && prev.next.val == dup) {
                prev.next = prev.next.next; // unlink, prev stays put
            }
        } else {
            prev = prev.next;
        }
    }

    return dummy.next;
}
```

```java
// Building a new list — dummy avoids a null check on the first append
public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
    ListNode dummy = new ListNode(0);
    ListNode tail = dummy;

    while (l1 != null && l2 != null) {
        if (l1.val <= l2.val) {
            tail.next = l1;
            l1 = l1.next;
        } else {
            tail.next = l2;
            l2 = l2.next;
        }
        tail = tail.next;
    }

    tail.next = (l1 != null) ? l1 : l2;

    return dummy.next;
}
```

```java
// Sentinel pair — doubly linked list with permanent head/tail (e.g., LRU Cache)
class DLinkedList {
    Node head = new Node(0, 0), tail = new Node(0, 0);

    DLinkedList() {
        head.next = tail;
        tail.prev = head;
    }

    void addFront(Node node) {          // insert right after head — no null checks needed
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }

    void remove(Node node) {            // unlink from anywhere — neighbors always exist
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }
}
```

## Practice Problems

| # | Problem | Link |
|---|---------|------|
| 1 | Remove Nth Node From End of List | [LeetCode #19](https://leetcode.com/problems/remove-nth-node-from-end-of-list/) |
| 2 | Merge Two Sorted Lists | [LeetCode #21](https://leetcode.com/problems/merge-two-sorted-lists/) |
| 3 | Remove Linked List Elements | [LeetCode #203](https://leetcode.com/problems/remove-linked-list-elements/) |
| 4 | Remove Duplicates from Sorted List | [LeetCode #83](https://leetcode.com/problems/remove-duplicates-from-sorted-list/) |
| 5 | Remove Duplicates from Sorted List II | [LeetCode #82](https://leetcode.com/problems/remove-duplicates-from-sorted-list-ii/) |
| 6 | Partition List | [LeetCode #86](https://leetcode.com/problems/partition-list/) |
| 7 | Add Two Numbers | [LeetCode #2](https://leetcode.com/problems/add-two-numbers/) |
| 8 | Swap Nodes in Pairs | [LeetCode #24](https://leetcode.com/problems/swap-nodes-in-pairs/) |
| 9 | Reverse Linked List II | [LeetCode #92](https://leetcode.com/problems/reverse-linked-list-ii/) |
| 10 | Insertion Sort List | [LeetCode #147](https://leetcode.com/problems/insertion-sort-list/) |
| 11 | Sort List | [LeetCode #148](https://leetcode.com/problems/sort-list/) |
| 12 | Merge k Sorted Lists | [LeetCode #23](https://leetcode.com/problems/merge-k-sorted-lists/) |
| 13 | Odd Even Linked List | [LeetCode #328](https://leetcode.com/problems/odd-even-linked-list/) |
| 14 | LRU Cache | [LeetCode #146](https://leetcode.com/problems/lru-cache/) |
| 15 | Flatten a Multilevel Doubly Linked List | [LeetCode #430](https://leetcode.com/problems/flatten-a-multilevel-doubly-linked-list/) |
