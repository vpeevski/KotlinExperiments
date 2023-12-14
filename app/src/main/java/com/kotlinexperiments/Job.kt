package com.kotlinexperiments

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
//    launch(Job()) { // the new job replaces one from parent
//        delay(1000)
//        println("Will not be printed")
//    }
    val job = Job()
    launch(job) { // the new job replaces one from parent
        delay(1000)
        println("Text 1")
    }
    launch(job) { // the new job replaces one from parent
        delay(2000)
        println("Text 2")
    }
//    job.join()
//    println("Never printed !!!")

    //### Instead Join all children of the job.
    job.children.forEach { it.join() }
    println("Print OK !!!")
}