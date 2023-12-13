package com.kotlinexperiments.flow

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

suspend fun main(): Unit = coroutineScope {
    val flow1 = flowOf(1, 2, 3, 4, 5)
    val flow2 = flowOf(6, 7, 8, 9, 10)
    val mergedFlow = flow1.merge(flow2)
    mergedFlow.collect {
        print(it)
    }
}

fun <T> Flow<T>.merge(other: Flow<T>): Flow<T> = channelFlow {
    // Concurrent execution in coroutine
    launch {
        collect {
            channel.send(it)
        }
    }
    other.collect {
        channel.send(it)
    }
}