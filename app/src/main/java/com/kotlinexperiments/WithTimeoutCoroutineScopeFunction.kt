package com.kotlinexperiments

import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

// withTimeout is also like coroutineScope. It also creates a scope and returns a value.
// Actually, withTimeout with a very big timeout behaves just like
// coroutineScope. The difference is that withTimeout additionally sets
// a time limit for its body execution. If it takes too long, it cancels
// this body and throws TimeoutCancellationException (a subtype of CancellationException).
suspend fun test(): Int = withTimeout(1500) {
    delayCancelable(1000)
    println("Still thinking")
    delayCancelable(1000)
    println("Done!")
    42
}

suspend fun main(): Unit = coroutineScope {
    try {
        test()
    } catch (e: TimeoutCancellationException) {
        println("Cancelled")
    }
    println("After Cancelled")

// Beware that withTimeout throws TimeoutCancellationException,
// which is a subtype of CancellationException (the same exception
// that is thrown when a coroutine is cancelled). So, when this
// exception is thrown in a coroutine builder, it only cancels it and
// does not affect its parent.
    val job = launch {
        test()
    }
    job.join()
    println("After Cancelled in coroutine builder")
    delayCancelable(1000) // Extra timeout does not help,
// `test` body was cancelled
}