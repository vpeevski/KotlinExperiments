package com.kotlinexperiments

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    launch {
        launch {
            delayCancelable(1000)
            throw Error("Some error")
        }
        launch {
            delayCancelable(2000)
            println("Will not be printed")
        }
        launch {
            delayCancelable(500) // faster than the exception
            println("Will be printed")
        }
    }
    launch {
        delayCancelable(2000)
        println("Will not be printed")
    }
}