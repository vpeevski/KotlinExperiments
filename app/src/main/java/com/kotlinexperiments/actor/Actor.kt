package com.kotlinexperiments.actor

import com.kotlinexperiments.massiveRun
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.coroutineScope

suspend fun main(): Unit = coroutineScope {
    val counter: SendChannel<CounterMsg> = counterActor()
    massiveRun { counter.send(IncCounter) }
    val response = CompletableDeferred<Int>()
    counter.send(GetCounter(response))
    println(response.await()) // 1000000
    response.invokeOnCompletion { ex ->
        println("On Completion CALLED, ex: $ex")
    }
    counter.close()
}

fun CoroutineScope.counterActor() = actor<CounterMsg> {
    var counter = 0
    for (msg in channel) {
        when (msg) {
            is IncCounter -> counter++
            is GetCounter -> msg.response.complete(counter)
        }
    }
}

sealed class CounterMsg
object IncCounter : CounterMsg()
class GetCounter(
    val response: CompletableDeferred<Int>
) : CounterMsg()