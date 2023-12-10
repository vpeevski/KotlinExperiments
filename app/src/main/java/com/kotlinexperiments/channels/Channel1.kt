package com.kotlinexperiments.channels

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

suspend fun main(): Unit = coroutineScope {
    val channel = Channel<Int>()
    launch {
        repeat(5) { index ->
            println("Producing next one")
            delay(1000)
            channel.send(index * 2)
        }
        channel.close()
    }
    launch {
        for (element in channel) {
            println("Consumed (in for): $element")
        }
// or
//        channel.consumeEach { element ->
//            println("Consumed (each): $element")
//        }
//        println("Channel closed: ${channel.isClosedForReceive}")
    }
}