package com.kotlinexperiments.flow.processing

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow

// filter returns a flow containing only values from the original
// flow that match the given predicate.
suspend fun main() {
    (1..10).asFlow() // [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
        .filter { it <= 5 } // [1, 2, 3, 4, 5]
        .filter { isEven(it) } // [2, 4]
        .collect { print(it) } // 24
    println()
    println("##### myFilter #####")
    (1..10).asFlow() // [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
        .myFilter { it <= 5 } // [1, 2, 3, 4, 5]
        .myFilter { isEven(it) } // [2, 4]
        .collect { print(it) } // 24
}

fun isEven(num: Int): Boolean = num % 2 == 0

// We would just need to introduce an if statement with the
// predicate (instead of transformation).
fun <T> Flow<T>.myFilter(predicate: (value: T) -> Boolean): Flow<T> = flow {
    collect {
        if (predicate(it)) {
            emit(it)
        }
    }
}

// NOTE: filter is typically used to eliminate elements we are not interested in.