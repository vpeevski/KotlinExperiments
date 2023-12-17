package com.kotlinexperiments.flow.hot

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

suspend fun main(): Unit = coroutineScope {
    val flow = flowOf("A", "B")
        .onEach { delay(1000) }
        .onEach { println("Produced $it") }
    val stateFlow: StateFlow<String> = flow.stateIn(
        scope = this,
        started = SharingStarted.Lazily,
        initialValue = "Empty"
    ) // Not suspending, but should have initial value
    println(stateFlow.value)
    delay(2000)
    stateFlow.collect { println("Received $it") }
}