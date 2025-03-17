// Define the node for the linked list
data class ListNode(val value: Int, var next: ListNode? = null)

// Iterative function to reverse the linked list
fun reverseListIter(head: ListNode?): ListNode? {
    var prev: ListNode? = null
    var current = head
    while (current != null) {
        val nextCurrent = current.next
        current.next = prev
        prev = current
        current = nextCurrent
    }
    return prev
}

// Iterative function to reverse the linked list
fun copyReversed(head: ListNode?): ListNode? {
    var prev: ListNode? = null
    var current = head
    while (current != null) {
        val nextCurrent = current.next
        val reversedCurrentCopy = ListNode(current.value, prev)
        reversedCurrentCopy.next = prev
        prev = reversedCurrentCopy
        current = nextCurrent
    }
    return prev
}

// Recursive function to reverse the linked list
fun reverseListRecursive(head: ListNode?): ListNode? {
    if (head?.next == null) return head
    val reversedList = reverseListRecursive(head.next)
    head.next?.next = head
    head.next = null
    return reversedList
}

// Utility function to print the linked list
fun printList(head: ListNode?) {
    var current = head
    while (current != null) {
        print("${current.value} ")
        current = current.next
    }
    println()
}

// Main function to demonstrate the reversal
fun main() {
    // Creating the linked list: 1 -> 2 -> 3 -> 4 -> null
    val node1 = ListNode(1)
    val node2 = ListNode(2)
    val node3 = ListNode(3)
    val node4 = ListNode(4)
    node1.next = node2
    node2.next = node3
    node3.next = node4

    println("Original List:")
    printList(node1)

    // Reverse the linked list
//    val reversedHeadIteractive = reverseListIter(node1)
    val reversedHeadIteractive = copyReversed(node1)
    val reversedRecursive = reverseListRecursive(node1)

    println("Reversed (Iteractive) List:")
    printList(reversedHeadIteractive)
    println("Reversed (Recursive) List:")
    printList(reversedRecursive)
}