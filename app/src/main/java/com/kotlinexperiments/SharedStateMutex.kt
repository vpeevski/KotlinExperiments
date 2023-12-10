package com.kotlinexperiments

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.system.measureTimeMillis

// When we use a dispatcher that is limited to a single thread, we don’t
// have such a problem. When a delay or a network call suspends a
// coroutine, the thread can be used by other coroutines -see SharedStateSingleThread.kt.
// IMPORTANT: This is why we avoid using mutex to wrap whole functions (coarse-grained approach).
class MessagesRepository {
    private val messages = mutableListOf<String>()
    private val mutex = Mutex()
    suspend fun add(message: String) = mutex.withLock {
        delay(1000) // Lock is not unlocked, no other coroutines can use thread.
        messages.add(message)
    }
}

suspend fun main() {
    val repo = MessagesRepository()
    val timeMillis = measureTimeMillis {
        coroutineScope {
            repeat(5) {
                launch {
                    repo.add("Message$it")
                }
            }
        }
    }
    println(timeMillis) // Take operations additive time: ~5120
}