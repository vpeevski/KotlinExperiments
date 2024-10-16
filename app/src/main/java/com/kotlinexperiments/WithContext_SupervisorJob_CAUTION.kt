package com.kotlinexperiments

import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

fun main() = runBlocking {
    println("Before")
    // USE supervisorScope INSTEAD !!!
    withContext(SupervisorJob()) { // CAUTION !!! JOB IS NOT INHERITED. IT CREATES A NEW JOB WITH PARENT SUPERVISOR WHICH IS USELESS
        launch {
            delayNotCancelable(1000)
            throw Error()
        }
        launch {
            delayNotCancelable(2000)
            println("Done")
        }
    }
    println("After")
}