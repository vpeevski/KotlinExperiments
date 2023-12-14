package com.kotlinexperiments.flow.processing

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.take


suspend fun main() {
    take()
    println()
    drop()
}

// We use take to pass only a certain number of elements.
suspend fun take() = ('A'..'Z').asFlow()
    .take(5) // [A, B, C, D, E]
    .collect { print(it) } // ABCDE

// We use drop to ignore a certain number of elements.
suspend fun drop() = ('A'..'Z').asFlow()
    .drop(20) // [U, V, W, X, Y, Z]
    .collect { print(it) } // UVWXYZ
