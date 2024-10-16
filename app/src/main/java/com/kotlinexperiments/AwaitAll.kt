package com.kotlinexperiments

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.isActive
import kotlinx.coroutines.job
import java.util.concurrent.CancellationException

suspend fun main(): Unit = coroutineScope {
    try {
        val results = coroutineScope {
            listOf(
                async {
                    try {
//                        delayCancelable(100)
                        kotlinx.coroutines.delay(100)
                        println("Async 1")
                        1
                    } catch (e: CancellationException) {
                        println("Async 1 Canceled")
                        throw e
                    }
                },
                async {
                    try {
//                        delayCancelable(200)
                        kotlinx.coroutines.delay(200)
                        println("Async 2")
//                        println("Async 2 state: $isActive")
                        2
                    } catch (e: CancellationException) {
                        println("Async 2 Canceled")
                        throw e
                    }
                },
                async {
                    try {
//                        delayCancelable(300)
                        kotlinx.coroutines.delay(300)
                        throw IllegalStateException("Exception in async 3")
//                        cancel()
//                        delayCancelable(300)
                        println("Async 3")
//                        cancel()
                        3
                    } catch (e: CancellationException) {
                        println("Async 3 Canceled")
                        throw e
                    }
                },
                async {
                    try {
//                        delayCancelable(400)
                        kotlinx.coroutines.delay(400)
                        delayNotCancelable(400)
                        println("Async 4")
                        println("Async 4 isCancelled: ${currentCoroutineContext().job.isCancelled}")
                        println("Async 4 isActive: ${currentCoroutineContext().job.isActive}")
                        println("Async 4 isCompleted: ${currentCoroutineContext().job.isCompleted}")
                        4
                    } catch (e: CancellationException) {
                        println("Async 4 Canceled")
                        throw e
                    }
                },
                async {
                    try {
//                        delayCancelable(500)
                        kotlinx.coroutines.delay(500)
                        println("Async 5")
                        println("Async 5 state: $isActive")
                        5
                    } catch (e: CancellationException) {
                        println("Async 5 Canceled")
                        throw e
                    }
                }).awaitAll()
        }
        println(results.fold("Results: ") { result, current -> "$result, $current" })
    } catch (e: CancellationException) {
        println("awaitAll Canceled")
        throw e
    } catch (e: IllegalStateException) {
        println("Some async failed: $e")
    } catch (e: Exception) {
        println("Caught exception during awaitAll: $e")
    }

//    try {
//        result.awaitAll()
//    } catch (e: CancellationException) {
//        println("awaitAll Canceled")
//    } catch (e: Exception) {
//        println("Caught exception during awaitAll: $e")
//    }


//    parentJob.invokeOnCompletion { e ->
//        if (e != null) {
//            println("parentJob completed with error: $e")
//        } else {
//            println("parentJob completed successfully")
//        }
//    }

    println("Done")
}