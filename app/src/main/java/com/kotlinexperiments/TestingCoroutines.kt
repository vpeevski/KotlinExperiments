package com.kotlinexperiments

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher

fun main() {
    // val scheduler = TestCoroutineScheduler()
    // val testDispatcher = StandardTestDispatcher(scheduler)
    val testDispatcher = StandardTestDispatcher()
    CoroutineScope(testDispatcher).launch {
        println("Some work 1")
        delayNotCancelable(1000)
        println("Some work 2")
        delayNotCancelable(1000)
        println("Coroutine done")
    }
    println("[${testDispatcher.scheduler.currentTime}] Before")
    testDispatcher.scheduler.advanceUntilIdle()
    println("[${testDispatcher.scheduler.currentTime}] After")
}