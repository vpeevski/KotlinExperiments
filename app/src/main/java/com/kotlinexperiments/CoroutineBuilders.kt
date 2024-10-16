package com.kotlinexperiments

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
// ### Async
//    val result: Deferred<Int> = GlobalScope.async {
//        delay(2000)
//        42
//    }
//    println("### Result: ${result.await()}")

// ### Structured concurrency - CoroutineScope, GlobalScope
    GlobalScope.launch {
        runBlocking {
            println("World Blocking")
        }
        delayNotCancelable(1000)
        println("World")
    }
    println("Hello")
    delayNotCancelable(2000) // Needed as GlobalScope coroutines are NOT is not waited to finish

    launch {
        delayNotCancelable(1000)
        println("World_1")
    }
    // No need to delay, launch is run in child scope of runBlocking scope.
    // So runBlocking is parent and waits for its child coroutine to finish to finish.
    println("Hello_1")

}