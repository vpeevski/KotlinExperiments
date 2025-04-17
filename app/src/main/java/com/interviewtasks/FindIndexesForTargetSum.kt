package com.interviewtasks

fun main() {
    println("############# Target sum indexes brute force ##################")
    val targetPairBruteForce = findTargetSumIndexes(listOf(5, 2, 6, 4, 1), 6)
    println("targetPair = $targetPairBruteForce")

    println("############# Target sum with hash set ##################")
    val targetPairWithSet = findTargetPairWithSet(listOf(5, 2, 6, 4, 1), 6)
    println("targetPairWithSet = $targetPairWithSet")

    println("############# Target sum with hash map ##################")
    val targetPairWithMap = findTargetPairMap(listOf(5, 2, 6, 4, 1), 6)
    println("targetPair = $targetPairWithMap")

}

private fun findTargetSumIndexes(nums: List<Int>, target: Int): Pair<Int, Int>? {
    for (i in nums.indices) {
        for (j in i + 1 until nums.size) {
            if (nums[i] + nums[j] == target) {
                return Pair(i, j)
            }
        }
    }
    return null // No matching pair found
}

private fun findTargetSumIndexesForEach(nums: List<Int>, target: Int): Pair<Int, Int>? {
    for (i in nums.indices) {
        for (j in i + 1 until nums.size) {
            if (nums[i] + nums[j] == target) {
                return Pair(i, j)
            }
        }
    }
    return null // No matching pair found
}

private fun findTargetPairMap(inputList: List<Int>, target: Int): Pair<Int, Int>? {
    val targetDiffToItem = mutableMapOf<Int, Int>()
    inputList.forEachIndexed { index, item ->
        targetDiffToItem[item]?.let { return Pair(it, index) }
        println("item: $item, at index: $index")
        targetDiffToItem[target - item] = index
        println("diffToTargetMap: $targetDiffToItem")
    }
    return null
}

private fun findTargetPairWithSet(inputList: List<Int>, target: Int): Boolean {
    val targets = HashSet<Int>()
    inputList.forEachIndexed { index, item ->
        targets.find { item == it }?.let { return true }
        println("item: $item, at index: $index")
        targets.add(target - item)
        println("targets: $targets")
    }
    return false
}