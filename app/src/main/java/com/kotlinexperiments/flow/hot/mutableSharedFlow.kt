package com.kotlinexperiments.flow.hot

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

suspend fun main(): Unit = coroutineScope {
    val mutableSharedFlow =
        MutableSharedFlow<String>(replay = 2)
// or MutableSharedFlow<String>()
    val job1 = launch {
        mutableSharedFlow.collect {
            println("#1 received $it")
        }
    }
    val job2 = launch {
        mutableSharedFlow.collect {
            println("#2 received $it")
        }
    }
    delay(1000)
    mutableSharedFlow.emit("Message1")
    mutableSharedFlow.emit("Message2")
    mutableSharedFlow.emit("Message3")

    delay(1000)
    val job3 = launch {
        mutableSharedFlow.collect {
            println("#3 received $it")
        }
    }
    job1.cancel()
    job2.cancel()
    job3.cancel()
}