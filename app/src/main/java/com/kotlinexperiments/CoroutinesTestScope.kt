package com.kotlinexperiments

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runCurrent

suspend fun main() = coroutineScope {
    val scope = TestScope()
    scope.launch {
        delayNotCancelable(1000)
        println("First done")
        delayNotCancelable(1000)
        println("Coroutine done")
    }
    println("[${scope.currentTime}] Before") // [0] Before
    scope.advanceTimeBy(1000)
    scope.runCurrent() // First done
    println("[${scope.currentTime}] Middle") // [1000] Middle
    scope.advanceUntilIdle() // Coroutine done
    println("[${scope.currentTime}] After") // [2000] After
}