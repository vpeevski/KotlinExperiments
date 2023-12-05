package com.kotlinexperiments

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

// Creates a new coroutine, but it suspends the previous one
// until the new one is finished, so it does not start any concurrent process.
// The provided scope inherits its coroutineContext from the outer
// scope, but it overrides the context’s Job. Thus, the produced scope
// respects its parental responsibilities:
//  • inherits a context from its parent;
//  • waits for all its children before it can finish itself;
//  • cancels all its children when the parent is cancelled.
@OptIn(ExperimentalTime::class)
suspend fun main(): Unit = coroutineScope {
    launch {
        delay(1000L)
        println("World!")
    }
    println("Hello,")

    // Both delay calls suspend parent coroutine scope
    val a = coroutineScope {
        delay(1000)
        10
    }
    println("a is calculated")
    val b = coroutineScope {
        delay(1000)
        20
    }
    println(a) // 10
    println(b)

    println("####################")
    launch(CoroutineName("Parent Coroutine Context")) {
        println("Before")
        val timeTaken = measureTime {
            longTask()
        }
        println("After: Time taken - $timeTaken")
    }
}

suspend fun longTask() = coroutineScope {
    launch {
        delayCancelable(1000)
        val name = coroutineContext[CoroutineName]?.name
        println("[$name] Finished task 1")
    }
    launch {
        delayCancelable(2000)
        val name = coroutineContext[CoroutineName]?.name
        println("[$name] Finished task 2")
    }
}