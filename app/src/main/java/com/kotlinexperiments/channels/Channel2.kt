package com.kotlinexperiments.channels

import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay

suspend fun main(): Unit = coroutineScope {
    val channel = produce {
        repeat(5) { index ->
            println("Producing next one")
            delay(1000)
            send(index * 2)
        }
    }
    for (element in channel) {
        println(element)
    }
    println("Channel closed: ${channel.isClosedForReceive}")
}