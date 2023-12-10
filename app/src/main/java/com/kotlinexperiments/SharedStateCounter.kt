package com.kotlinexperiments

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicInteger

var counter = 0
var counterWithMutex = 0
private var counterAtomic = AtomicInteger()

fun main() = runBlocking {
    println("####### Synchronized block ######")
    val lock = Any()
    massiveRun {
        // Works OK but:
        // 1. Inside synchronized block we cannot use suspending functions
        // 2. We are blocking threads!
        synchronized(lock) {
            counter++
        }
    }
    println("Synchronized block counter: $counter") // ~567231

    println("####### Mutex ######")
    val mutex = Mutex()
    massiveRun {
        // 1. Suspend a coroutine instead of blocking a thread.
        // 2. Starts with lock but calls unlock on the finally block
        //      so that any exceptions thrown inside the block will
        //      successfully release the lock.
        // 3. Better performance (IN SOME CASES) compared with dispatcher with
        //      parallelism limited to a single thread
        // 4. DANGER - NOT RE-ENTRANT !!! A coroutine cannot get past the lock twice.
        // 5. PROBLEM !!! Not unlocked when a coroutine is suspended - see SharedStateMutex.kt
        mutex.withLock {
            counterWithMutex++
        }
    }
    println("Mutex counter: $counterWithMutex")

    println("####### Atomics ######")
    massiveRun {
        counterAtomic.incrementAndGet()
    }
    println("Atomics counter: ${counterAtomic.get()}")
}

suspend fun massiveRun(action: suspend () -> Unit) =
    withContext(Dispatchers.Default) {
        repeat(1000) {
            launch {
                repeat(1000) { action() }
            }
        }
    }