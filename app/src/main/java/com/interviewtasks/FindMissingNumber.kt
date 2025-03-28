package com.interviewtasks

fun findMissingNumber(numbers: List<Int>, maxNumber: Int): Int =
    sumN(maxNumber) - numbers.sum()

fun sumN(n: Int) = (1 + n) * n / 2

fun findMissingNumberNaive(numbers: List<Int>, maxNumber: Int): Int {
    val sortedNumbers = numbers.sorted()
    var lastCheked: Int = 0
    sortedNumbers.forEach { current ->
        if (current - lastCheked == 1) {
            lastCheked = current
        } else {
            return@findMissingNumberNaive current - 1
        }
    }
    throw IllegalStateException("No missing number found!")
}

fun main() {
    val maxNumber = 7
    val numbers = listOf(1, 7, 2, 4, 5, 6)
    println("Sum N is: ${sumN(maxNumber)}")
    val missingNumber = findMissingNumber(numbers, maxNumber)
    println("Missing number is: $missingNumber")
    val missingNumberNaive = findMissingNumberNaive(numbers, maxNumber)
    println("Missing number naive is: $missingNumberNaive")
}