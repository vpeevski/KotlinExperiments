package com.kotlinexperiments.flow.hot

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

// Way to turn one flow into multiple flows - shareIn.
// Turn a Flow into a SharedFlow is by using the shareIn function
suspend fun main(): Unit = coroutineScope {
    val flow = flowOf("A", "B", "C")
        .onEach { delay(1000) }
    val sharedFlow: SharedFlow<String> = flow.shareIn(
        scope = this,
        started = SharingStarted.Eagerly,
// replay = 0 (default)
    )
    delay(500)
    launch {
        sharedFlow.collect { println("#1 $it") }
    }
    delay(1000)
    launch {
        sharedFlow.collect { println("#2 $it") }
    }
    delay(1000)
    launch {
        sharedFlow.collect { println("#3 $it") }
    }
}