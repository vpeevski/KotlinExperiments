package com.kotlinexperiments.flow.processing.aggregation

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.scan

// scan is an alternative to fold.
// It is intermediate operation (not terminal)
// that produces all intermediate accumulator values.
suspend fun main() {
    val list = listOf(1, 2, 3, 4, 5)
    val res = list.scan(0) { acc, i -> acc + i }
    println("##### Iterable scan #####")
    println(res) // [0, 1, 3, 6, 10, 15]

    // scan is useful with Flow because it produces a new value immediately
    // after receiving one from the previous step.
    println("##### Flow scan #####")
    flowOf(1, 2, 3, 4, 5)
        .onEach { delay(1000) }
        .scan(0) { acc, v -> acc + v }
        .collect { println("Current sum is: $it") }
    println("##### Flow myScan #####")
    flowOf(1, 2, 3, 4, 5)
        .onEach { delay(1000) }
        .myScan(0) { acc, v -> acc + v }
        .collect { println("Current sum is: $it") }

}

// We can implement scan by using the flow builder and collect.
// We first emit the initial value, then with each new element we emit the
// result of the next value accumulation.
fun <T, R> Flow<T>.myScan(initial: R, operation: suspend (acc: R, next: T) -> R): Flow<R> = flow {
    var acc = initial
    emit(initial)
    collect {
        acc = operation(acc, it)
        emit(acc)
    }
}

// The typical use case for scan is when we have a flow of updates or
// changes, and we need an object that is the result of these changes.
// val userStateFlow: Flow<User> = userChangesFlow
//     .scan(user) { user, change -> user.withChange(change) }
//
// val messagesListFlow: Flow<List<Message>> = messagesFlow
//     .scan(messages) { acc, message -> acc + message }

