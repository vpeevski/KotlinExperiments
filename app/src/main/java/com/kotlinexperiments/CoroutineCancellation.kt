package com.kotlinexperiments

import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

suspend fun main(): Unit = coroutineScope {
    val job = launch {
        repeat(1_000) { i ->
//            if (!isActive) return@launch
            delayCancelable(200)
            println("Printing $i")
        }
    }
    delay(1100)
//    job.cancel()
//    job.join()
    job.cancelAndJoin()
    println("Cancelled successfully")
}