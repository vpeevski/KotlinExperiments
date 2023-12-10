package com.kotlinexperiments

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.system.measureTimeMillis

suspend fun main() {
    // The code below takes around 2 second because
    // Dispatchers.IO allows around 50 active threads at the same time.
    val time = measureTimeMillis {
        coroutineScope {
            repeat(100) {
                launch(Dispatchers.IO) {
                    Thread.sleep(1000)
                }
            }
        }
    }
    println("Execution time: $time") // ~2000

    println("################")

    // The limit of Dispatchers.IO is 64 (or the number of cores if there are more).
    coroutineScope {
        repeat(100) {
            launch(Dispatchers.IO) {
                Thread.sleep(200)
                val threadName = Thread.currentThread().name
                println("Running on thread: $threadName")
            }
        }
    }

    // Both Dispatchers.Default and Dispatchers.IO
    // share the same pool of threads. This is an important optimization.
    // Threads are reused, and often redispatching is not needed.
    // For instance, let’s say you are running on Dispatchers.Default and then
    // execution reaches withContext(Dispatchers.IO) { ... }.
    // Most often, you will stay on the same thread.
    // What changes is that  this thread counts not towards the Dispatchers.Default
    // limit but towards the Dispatchers.IO limit. Their limits are independent, so
    // they will never starve each other.
    println("####### withContext #########")
    coroutineScope {
        launch(Dispatchers.Default) {
            println(Thread.currentThread().name)
            withContext(Dispatchers.IO) {
                println(Thread.currentThread().name)
            }
        }
    }
}