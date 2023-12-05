//package com.kotlinexperiments
//
//import kotlinx.coroutines.Job
//import kotlinx.coroutines.coroutineScope
//import kotlinx.coroutines.launch
//
//suspend fun main(): Unit = coroutineScope {
//    val job = Job()
//
//    launch(job) {
//        repeat(5) { num ->
//            delay(200) // Already started before completion
//            println("Rep$num")
//        }
//    }
//
//    launch {
//        delay(500)
//        if (job.complete()) {
//            println("Job completed: $job")
//        }
//
//    }
//
//    job.join()
//
//    launch(job) {
//        println("Will not be printed, Coroutine started after completion.")
//    }
//
//    println("Done !")
//}