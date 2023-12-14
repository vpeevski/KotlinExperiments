package com.kotlinexperiments.flow.processing.terminal

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.reduce

// Terminal operations (that end flow processing) are suspended
// and they return the value once the flow is complete
// (or they complete the flow themselves).
suspend fun main() {
    val flow = flowOf(1, 2, 3, 4) // [1, 2, 3, 4]
    println(flow.first()) // 1
    println(flow.count()) // 4
    println(flow.reduce { acc, value -> acc * value }) // 24
    println(flow.fold(0) { acc, value -> acc + value }) // 10
    println("Custom terminal operation sum: ${flow.sum()}") // 10
}

// Custom terminal operation
suspend fun Flow<Int>.sum(): Int {
    var sum = 0
    collect { value ->
        sum += value
    }
    return sum
}