package com.kotlinexperiments.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext

// Sequence is not suitable for working with coroutines,
// as its terminal operation forEach is not an suspend function.
fun getSequence(): Sequence<String> = sequence {
    repeat(3) {
        Thread.sleep(1000)
// the same result as if there were delay(1000) here
        yield("User$it")
    }
}

suspend fun main() {
    withContext(newSingleThreadContext("main")) {
        launch { // Coroutine execution is blocked.
            repeat(3) {
                delay(100)
                println("Processing on coroutine")
            }
        }
        val list = getSequence()
        list.forEach { println(it) } // Not suspendable, blocks thread.
    }
}