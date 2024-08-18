package com.kotlinexperiments.dispatchers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withContext

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun main(): Unit = coroutineScope {
    Dispatchers.setMain(Dispatchers.IO)
//    println("##### CONFINED DISPATCHERS - Main, IO, Default")
//    launch(Dispatchers.Main) {
//        println("Main dispatcher : I'm working in thread ${Thread.currentThread().name}")
//        delay(1000)
//        println("Main dispatcher : After delay in thread ${Thread.currentThread().name}")
//    }
//    launch(Dispatchers.IO) {
//        println("IO dispatcher   : I'm working in thread ${Thread.currentThread().name}")
//        delay(1000)
//        println("IO dispatcher   : After delay in thread ${Thread.currentThread().name}")
//    }
//    launch(Dispatchers.Default) {
//        println("Default dispatcher: I'm working in thread ${Thread.currentThread().name}")
//        delay(1000)
//        println("Default dispatcher: After delay in thread ${Thread.currentThread().name}")
//    }

    println("##### UNCONFINED DISPATCHER - Unconfined")
    withContext(Dispatchers.IO) {
        launch(Dispatchers.Unconfined) {
            println("Unconfined : I'm working in thread ${Thread.currentThread().name}")
            delay(1000)
            println("Unconfined : After delay in thread ${Thread.currentThread().name}")
        }
    }
}