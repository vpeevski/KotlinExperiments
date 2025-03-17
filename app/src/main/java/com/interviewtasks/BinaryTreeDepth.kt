package com.interviewtasks

import kotlin.math.max

data class BinaryTreeNode(
    val value: Int,
    var left: BinaryTreeNode? = null,
    var right: BinaryTreeNode? = null
)

private fun createTree(): BinaryTreeNode {
    val root = BinaryTreeNode(1)
    val node2 = BinaryTreeNode(2)
    val node3 = BinaryTreeNode(3)
    val node4 = BinaryTreeNode(4)
    val node5 = BinaryTreeNode(5)
    val node6 = BinaryTreeNode(6)
    val node7 = BinaryTreeNode(7)
    val node8 = BinaryTreeNode(8)
    val node9 = BinaryTreeNode(9)
    val node10 = BinaryTreeNode(10)
    val node11 = BinaryTreeNode(11)
    val node12 = BinaryTreeNode(12)
    val node13 = BinaryTreeNode(13)
    val node14 = BinaryTreeNode(14)
    val node15 = BinaryTreeNode(15)
    val node16 = BinaryTreeNode(16)
    val node17 = BinaryTreeNode(17)
    val node18 = BinaryTreeNode(18)
    root.left = node2
    root.right = node3
    node2.left = node4
    node2.right = node5
    node3.left = node6
    node3.right = node7
    node4.left = node8
    node4.right = node9
    node5.left = node10
    node5.right = node11
    node6.left = node12
    node6.right = node13
    node7.left = node14
    node7.right = node15
    node8.left = node16
    node8.right = node17
    node9.left = node18
    return root
}

fun main() {
    val root = createTree()
    printTree(root)
//    printBinaryTree(root)
    println("Max depth is: ${maxDepth(root)}")
}

private fun maxDepth(root: BinaryTreeNode?): Int {
    if (root == null) return 0
    return 1 + max(maxDepth(root.left), maxDepth(root.right))
}

fun printTree(root: BinaryTreeNode?) {
    if (root == null) return

    // Determine the height of the tree.
    val height = maxDepth(root)
    val maxNodes = 1 shl height

    // Start a level-order traversal.\
    var currentLevel = mutableListOf(root)
    var initialSpacingForCurrentRow = maxNodes
    var previousPrintedLevel = listOf(PositionedNode(initialSpacingForCurrentRow, root))
    for (heightLevel in 1..height) {
        // Calculate spacing between node values.
        val printedNodes = printNodesLine(
            heightLevel,
//            initialSpacingForCurrentRow,
            previousPrintedLevel,
            currentLevel
        )
        // If not the last level, print the connectors line.
        if (heightLevel < height) {
            printConnectorsLine(printedNodes)
        }
        previousPrintedLevel = printedNodes
        currentLevel = nextLevel(currentLevel)
        initialSpacingForCurrentRow /= 2
    }

    println("Width: ${maxNodes / 2}")
}

fun printNodesLine(
    heightLevel: Int,
//    initialSpacing: Int,
    previousLevel: List<PositionedNode>,
    currentLevel: MutableList<BinaryTreeNode>
): List<PositionedNode> {
    val positionedNodes = mutableListOf<PositionedNode>()
//    val line = StringBuilder()
//    currentLevel.forEachIndexed { index, node ->
//        val parentIndex = index / 2
//        val parentNodeOffset = if (index % 2 == 0) -2 else 2
//        val nodeSpacing = if (index % 2 == 0) previousLevel[parentIndex].lineOffset + -2 else positionedNodes.last()
////        line.append(" ".repeat(nodeSpacing))
////        positionedNodes.add(
////            PositionedNode(
////                lineOffset = nodeSpacing,
////                node = node
////            )
////        )
////        line.append(node.value.toString())
//    }
//    println(line.toString())
    return positionedNodes
}

fun printConnectorsLine(
    currentLevel: List<PositionedNode>
) {
    val connectorLine = StringBuilder()
    currentLevel.forEachIndexed { index, positionedNode ->
        connectorLine.append(" ".repeat(positionedNode.lineOffset - if (index == 0) 1 else 2))
        connectorLine.append(if (positionedNode.node.left != null) "/" else " ")
        connectorLine.append(" ")
        connectorLine.append(if (positionedNode.node.right != null) "\\" else " ")
    }
    println(connectorLine.toString())
}

fun nextLevel(currentLevel: MutableList<BinaryTreeNode>): MutableList<BinaryTreeNode> {
    val nextLevel = mutableListOf<BinaryTreeNode>()
    for (node in currentLevel) {
        node.left?.let {
            nextLevel.add(it)
        }
        node.right?.let {
            nextLevel.add(it)
        }
    }
    return nextLevel
}

data class PositionedNode(val lineOffset: Int, val node: BinaryTreeNode)


// Compute the height of the tree
//fun getHeight(root: BinaryTreeNode?): Int {
//    if (root == null) return 0
//    return 1 + maxOf(getHeight(root.left), getHeight(root.right))
//}
//
//// Recursively fill the matrix with node values
//fun fillMatrix(
//    root: BinaryTreeNode?,
//    matrix: MutableList<MutableList<String>>,
//    row: Int,
//    left: Int,
//    right: Int
//) {
//    if (root == null || left > right) return
//    // Place the current node's value in the middle of the current range
//    val mid = (left + right) / 2
//    matrix[row][mid] = root.value.toString()
//
//    // Fill in the left and right subtrees
//    fillMatrix(root.left, matrix, row + 1, left, mid - 1)
//    fillMatrix(root.right, matrix, row + 1, mid + 1, right)
//}
//
//// Function to print the binary tree in a structured format
//fun printBinaryTree(root: BinaryTreeNode) {
//    val height = getHeight(root)
//    // Calculate the width: (2^height) - 1
//    val width = (1 shl height) - 1
//    // Create a 2D matrix filled with spaces
//    val matrix = MutableList(height) { MutableList(width) { " " } }
//
//    // Fill the matrix with the tree nodes
//    fillMatrix(root, matrix, 0, 0, width - 1)
//
//    // Print each row of the matrix
//    for (row in matrix) {
//        println(row.joinToString(""))
//    }
//}

