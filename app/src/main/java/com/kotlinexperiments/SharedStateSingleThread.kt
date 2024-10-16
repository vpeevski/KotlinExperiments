package com.kotlinexperiments

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.system.measureTimeMillis

class MessagesRepositorySingleThread {
    private val messages = mutableListOf<String>()
    private val dispatcher = Dispatchers.IO
        .limitedParallelism(1)

    suspend fun add(message: String) =
        withContext(dispatcher) {
            delayNotCancelable(1000) // Other coroutines can use the thread!
            messages.add(message)
        }
}

suspend fun main() {
    val repo = MessagesRepositorySingleThread()
    val timeMillis = measureTimeMillis {
        coroutineScope {
            repeat(5) {
                launch {
                    repo.add("Message$it")
                }
            }
        }
    }
    println(timeMillis) // 1058
}