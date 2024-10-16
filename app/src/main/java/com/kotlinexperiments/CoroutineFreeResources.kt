package com.kotlinexperiments


import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

suspend fun main(): Unit = coroutineScope {
    val job = launch {
        delayCancelable(1000)
    }

    job.invokeOnCompletion { ex: Throwable? ->
        println("Finished: $ex") // ex is null if completed normally
        println("Always called: Do cleanup here...")
    }
    delayNotCancelable(400)
    job.cancelAndJoin()
}