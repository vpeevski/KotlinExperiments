package com.kotlinexperiments

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    val handler =
        CoroutineExceptionHandler { ctx, exception ->
            println("Caught $exception")
        }
    val scope = CoroutineScope(handler)
    scope.launch {
        delayNotCancelable(1000)
        throw Error("Some error")
    }
    scope.launch {
        delayNotCancelable(2000)
        println("Will be printed")
    }
    delayNotCancelable(3000)
}