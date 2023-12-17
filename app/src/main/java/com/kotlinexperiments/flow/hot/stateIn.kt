package com.kotlinexperiments.flow.hot

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

suspend fun main(): Unit = coroutineScope {
    val flow = flowOf("A", "B", "C")
        .onEach { delay(1000) }
        .onEach { println("Produced $it") }
    val stateFlow: StateFlow<String> = flow.stateIn(this) // suspending function
    println("Listening")
    println(stateFlow.value)
    launch {
        stateFlow.collect { println("#1 Received $it") }
    }
    launch {
        stateFlow.collect { println("#2 Received $it") }
    }
}