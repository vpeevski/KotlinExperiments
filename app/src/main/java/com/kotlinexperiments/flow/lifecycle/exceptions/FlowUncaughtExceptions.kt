package com.kotlinexperiments.flow.lifecycle.exceptions

import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

// Uncaught exceptions in a flow immediately cancel this flow, and
// collect rethrows this exception. This behavior is typical of suspending
// functions, and coroutineScope behaves the same way. Exceptions
// can be caught outside flow using the classic try-catch block.
val dataFlow = flow {
    emit("Message1")
    throw MyError()
}

suspend fun main() {
    println("####### Flow builder throws #######")
    try {
        dataFlow
            .collect { println("Collected $it") }
    } catch (e: MyError) {
        println("Caught 1: $e")
    }

    // Notice that using Flow.catch does not protect us from an exception in the
    // terminal operation (because catch cannot be placed after the last
    // operation). So, if there is an exception in the collect, it won’t be
    // caught, and an error will be thrown.
    println("####### collect throws #######")
    val dataFlow1 = flow {
        emit("Value1")
        emit("Value2")
    }
    try {
        dataFlow1
            .onStart { println("Start...") }
            .catch { println("Caught: $it") }
            .collect(throw MyError())
    } catch (ex: MyError) {
        println("Caught 2: $ex")
    }


    // Therefore, it is common practice to move the operation from
    // collect to onEach and place it before the catch. This is specifically
    // useful if we suspect that collect might raise an exception. If we
    // move the operation from collect, we can be sure that catch will
    // catch all exceptions.
    println("####### onEach throws #######")
    dataFlow1
        .onStart { println("Start...") }
        .onEach { throw MyError() }
        .catch { println("Caught 3: $it") }
        .collect {}
}