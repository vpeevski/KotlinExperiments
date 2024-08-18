package com.kotlinexperiments.dispatchers

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

// If you don’t set any dispatcher, the one chosen by default is
// Dispatchers.Default, which is designed to run CPU-intensive
// operations. It has a pool of threads with a size equal to the number
// of cores in the machine your code is running on (but not less than two).
suspend fun main(): Unit = coroutineScope {
    repeat(20) {
        launch { // or launch(Dispatchers.Default) {
            // To make it busy
            List(50) { Random.nextLong() }
            val threadName = Thread.currentThread().name
            println("Running on thread: $threadName")
        }
    }

// Warning: runBlocking sets its own dispatcher if no other
// one is set; so, inside it, the Dispatcher.Default is not
// the one that is chosen automatically. So, if we used
// runBlocking instead of coroutineScope in the above
// example, all coroutines would be running on “main”.
    runBlocking {
        repeat(20) {
            launch { // or launch(Dispatchers.Default) {
                // To make it busy
                List(100) { Random.nextLong() }
                val threadName = Thread.currentThread().name
                println("Running on thread: $threadName")
            }
        }
    }
}