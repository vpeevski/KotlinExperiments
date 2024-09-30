package com.kotlinexperiments.flow.processing

import com.kotlinreflection.Box
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

// map transforms each flowing element according to its transformation function
// Its use cases include unpacking or converting values into a different type.
suspend fun main() {
    flowOf(1, 2, 3) // [1, 2, 3]
        .map { it * it } // [1, 4, 9]
        .collect { print(it) } // 149
    println()
    println("##### myMap #####")
    flowOf(1, 2, 3)
        .myMap { it + it }
        .collect { print(it) }

    println()
    println("##### myFilter1 #####")
    flowOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
        .myFilter1 { it % 2 == 0 }
        .collect { print(it) }
}

// To implement map, we might use the flow builder to create a new flow.
// Then, we might collect elements from the previous flow and emit each
// collected element transformed.
suspend fun <T, R> Flow<T>.myMap(transform: suspend (value: T) -> R): Flow<R> = flow {
    collect {
        emit(transform(it))
    }
}

suspend fun <T> Flow<T>.myFilter1(predicate: suspend (value: T) -> Boolean): Flow<T> = flow {
    collect {
        if (predicate(it)) {
            emit(it)
        }
    }
}