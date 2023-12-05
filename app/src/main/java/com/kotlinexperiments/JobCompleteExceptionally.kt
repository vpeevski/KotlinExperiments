//package com.kotlinexperiments
//
//import kotlinx.coroutines.Job
//import kotlinx.coroutines.coroutineScope
//import kotlinx.coroutines.launch
//
//suspend fun main(): Unit = coroutineScope {
//    val job = Job()
//    launch(job) {
//        repeat(5) { num ->
//            delay(200)
//            println("Job$num")
//        }
//    }
//
//    launch {
//        delay(500)
//        job.completeExceptionally(Error("Error message"))
//    }
//
//    job.join()
//
//    launch(job) {
//        println("Not printed, launched after completion with exception!")
//    }
//
//    println("Done !")
//}