package com.kotlinexperiments

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope

fun main(): Unit = runBlocking {
// Don't wrap in a try-catch here. It will be ignored.
//    try {
//        launch {
//            delayCancelable(1000)
//            throw Error("Some error")
//        }
//    } catch (e: Throwable) { // nope, does not help here
//        println("Will not be printed")
//    }
//    launch {
//        delayCancelable(2000)
//        println("Will not be printed")
//    }


// Instead use SupervisorJob: stops coroutines breaking by ignoring all exceptions in its children
    val supervisorScope = CoroutineScope(SupervisorJob())
    supervisorScope.launch {
        delayCancelable(1000)
        throw Error("Some error")
    }
    supervisorScope.launch {
        delayCancelable(2000)
        println("Will be printed")
    }
    delayCancelable(3000)

    println("#####################")

    // Don't do that, SupervisorJob with one children
    // and no parent works similar to just Job
    launch(SupervisorJob()) {
        launch {
            delayCancelable(1000)
            throw Error("Some error")
        }
        launch {
            delayCancelable(2000)
            println("Will not be printed")
        }
    }
    delayCancelable(3000)

    println("####### Instead do this #######")

    // Instead do this
    val job = SupervisorJob(coroutineContext.job) // Has parent, otherwise need join.
    launch(job) {
        delay(1000)
        throw Error("Some error")
    }
    launch(job) {
        delay(2000)
        println("Will be printed")
    }
//    job.join() // Needed if supervisor job does not have current job from runBlocking as parent
}