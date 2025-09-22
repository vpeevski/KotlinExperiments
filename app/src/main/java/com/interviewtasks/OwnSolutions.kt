package com.interviewtasks

import kotlin.math.max

private fun findSum(arr: Array<Int>, targetSum: Int): Pair<MyPair<Int, Int>, MyPair<Int, Int>>? {
    val diffMap = mutableMapOf<Int, MyPair<Int, Int>>()
    var result: Pair<MyPair<Int, Int>, MyPair<Int, Int>>? = null
    for (index in 0..arr.lastIndex) {
        val elem = arr[index]
        val searchedDiff = diffMap[elem]
        if (searchedDiff == null) {
            diffMap[targetSum - elem] = MyPair(index, elem)
        } else {
            result = Pair(searchedDiff, MyPair(index, elem))
            break
        }
    }
    return result
}

// list values 1..max but one value is missing
fun findMissing(list: List<Int>, max: Int): Int {
    val sorted = list.sorted()
    for (index in list.indices) {
        if (index + 1 != sorted[index]) {
            return sorted[index] - 1
        }
    }
    return max
}

data class TreeNode<T>(val value: T, val left: TreeNode<T>?, val right: TreeNode<T>?)

fun <T> maxDepth(treNode: TreeNode<T>?): Int {
    if (treNode == null) return 0
    return 1 + max(maxDepth(treNode.left), maxDepth(treNode.right))
}

sealed interface Bracket {
    enum class OPENING(val ch: Char) : Bracket {
        SMALL('('),
        MIDDLE('['),
        LARGE('{'),
        ;

        companion object {
            fun fromChar(ch: Char): OPENING? = when (ch) {
                '(' -> SMALL
                '[' -> MIDDLE
                '{' -> LARGE
                else -> null
            }
        }

        override fun toString(): String {
            return ch.toString()
        }
    }

    enum class CLOSING(val ch: Char, val openingBracket: OPENING) : Bracket {
        SMALL(')', OPENING.SMALL),
        MIDDLE(']', OPENING.MIDDLE),
        LARGE('}', OPENING.LARGE),
        ;

        companion object {
            fun fromChar(ch: Char): CLOSING? = when (ch) {
                ')' -> SMALL
                ']' -> MIDDLE
                '}' -> LARGE
                else -> null
            }
        }

        override fun toString(): String {
            return ch.toString()
        }
    }

    companion object {
        fun fromChar(ch: Char): Bracket? = OPENING.fromChar(ch) ?: CLOSING.fromChar(ch)
    }


}

data class BracketInText(val bracket: Bracket, val position: Int)

sealed interface BracketsResult {

    data object BalancedBracketsResult : BracketsResult {
        override fun toString(): String {
            return "Balanced: Yes"
        }
    }

    data class UnbalancedBracketsResult(
        val unbalancedBracket: BracketInText
    ) : BracketsResult {
        override fun toString(): String {
            return when (val bracket = unbalancedBracket.bracket) {
                is Bracket.OPENING -> "Not closed bracket $bracket at position: ${unbalancedBracket.position}"
                is Bracket.CLOSING -> "No opening bracket for $bracket at position: ${unbalancedBracket.position}"
            }
        }
    }


}

fun balancedBrackets(str: String): BracketsResult {
    val stack = ArrayDeque<BracketInText>()
    str.forEachIndexed { index, char ->
        when (val currentBracket = Bracket.fromChar(char)) {
            is Bracket.OPENING -> stack.addLast(
                BracketInText(
                    bracket = currentBracket,
                    position = index
                )
            )

            is Bracket.CLOSING -> when (stack.removeLastOrNull()?.bracket) {
                currentBracket.openingBracket -> {}
                else -> return BracketsResult.UnbalancedBracketsResult(
                    unbalancedBracket = BracketInText(
                        bracket = currentBracket,
                        position = index
                    )
                )
            }

            null -> {}
        }
    }
    return if (stack.isEmpty())
        BracketsResult.BalancedBracketsResult
    else
        BracketsResult.UnbalancedBracketsResult(
            unbalancedBracket = stack.removeLast()
        )
}

fun main() {
//  Find sum
//    val sum = 10
//    val elements = arrayOf(5, 2, 7, 9, 8)
//    val result = findSum(elements, sum)
//    println("Sum: $sum -> $result")

    // Find missing
//    val missing = findMissing(listOf(1, 5, 3, 4, 10, 8, 7, 2, 9), 10)
//    println("Missing number is: $missing")

    // Balanced brackets in text
    val balancedResult =
        balancedBrackets("Hello { This is [ really ( stupid example ) for ] balanced text }")
    println(balancedResult)
}

class MyPair<out A, out B>(val first: A, val second: B) {
    override fun toString(): String {
        return "[index: $first, value: $second]"
    }
}
