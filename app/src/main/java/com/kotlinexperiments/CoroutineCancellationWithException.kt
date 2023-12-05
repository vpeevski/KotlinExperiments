package com.kotlinexperiments

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
//import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

suspend fun main(): Unit = coroutineScope {
    val job = Job()
    launch(job) {
        try {
            delayCancelable(2000)
            println("Done - NOT PRINTED because of the cancellation")
        } catch (e: CancellationException) {
            // Still coroutine is running. Cleanup resources here.
            println("### Job CANCELED: $e")
            // But no suspension is allowed at this point, after cancellation.
            // Also no new coroutines can be started after cancellation.
            launch {
                println("This is ignored - NOT PRINTED !")
            }

            withContext(NonCancellable) {
                delayCancelable(1000)
                println("Catch: LOGGING or ERROR Processing - NOT CANCELABLE !!!")
            }

            delayCancelable(1000)
            println("NOT PRINTED: Cancellation exception thrown instead !")
            throw e
        } finally {
            println("Finally: Will always be printed")
            withContext(NonCancellable) {
                delayCancelable(1000)
                println("Finally: Cleanup - NOT CANCELABLE !!!")
            }
        }
    }
    delay(1000)
    job.cancelAndJoin()
    println("Cancel successfully !!!")
}