package com.kotlinexperiments

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

object MyNonPropagatingException : CancellationException()

suspend fun main(): Unit = coroutineScope {
    launch { // 1
        launch { // 2
            delayCancelable(2000)
            println("Will not be printed")
        }
        throw MyNonPropagatingException // 3
    }
    launch { // 4
        delayCancelable(2000)
        println("Will be printed")
    }
}