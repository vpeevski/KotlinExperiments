package com.kotlinexperiments

import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

suspend fun main(): Unit = coroutineScope {
    // 1. By using yield suspend function
    val job1 = Job()
    launch(job1) {
        repeat(1_000) { i ->
            Thread.sleep(200) // We might have some complex operations or reading files here
            yield()
            println("Printing $i")
        }
    }
    delay(1000)
    job1.cancelAndJoin()
    println("Cancelled successfully 1")
    delay(1000)

    println("####################")

    // 2. By using isActive extension property of the
    val job2 = Job()
    launch(job2) {
        do {
            Thread.sleep(200) // We might have some complex operations or reading files here
            println("Printing...")
        } while (isActive)
    }
    delay(1000)
    job2.cancelAndJoin()
    println("Cancelled successfully 2")
    delay(1000)

    println("####################")

    // 3. By using ensureActive() state

    val job3 = Job()
    launch(job3) {
        repeat(1000) { num ->
            Thread.sleep(200)
            ensureActive()
            println("Printing $num")
        }
    }
    delay(1000)
    job3.cancelAndJoin()
    println("Cancelled successfully 3")
}