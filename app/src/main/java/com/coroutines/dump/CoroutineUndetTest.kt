package com.coroutines.dump

import com.kotlinexperiments.delayNotCancelable
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow

class Counter {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { context, t ->
        val coroutineName = context[CoroutineName.Key]?.name ?: "Unknown"
        println("Counter exception in coroutine [$coroutineName], error: $t")
    }

    private val customCoroutineScope =
        CoroutineScope(CoroutineName("Parent") + coroutineExceptionHandler)

    private val _counterState =
        MutableStateFlow(0)
    val counterState: SharedFlow<Int> = _counterState

    suspend fun incCounter() {
        customCoroutineScope.async {
            delayNotCancelable(2000)
            throw IllegalStateException("Counter error")
            _counterState.value += 1
        }.await()
    }
}

suspend fun main(): Unit = coroutineScope {
    Counter().incCounter()
}