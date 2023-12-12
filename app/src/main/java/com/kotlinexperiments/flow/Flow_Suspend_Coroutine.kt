package com.kotlinexperiments.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext

// Flow should be used for streams of data that need to use coroutines.
// The flow builder is not suspending
// and does not require any scope. It is the terminal operation that is
// suspending and builds a relation to its parent coroutine (similar to
// the coroutineScope function)
fun getFlow(): Flow<String> = flow {
    repeat(3) {
        delay(1000)
        emit("User$it")
    }
}

suspend fun main() {
    withContext(newSingleThreadContext("main")) {
        launch { // Coroutine executed while flow terminal operation is suspended.
            repeat(3) {
                delay(100)
                println("Processing on coroutine")
            }
        }
        val list = getFlow()
        list.collect { println(it) }// Suspend coroutine instead of blocking thread.
    }
}