package com.kotlinexperiments.flow.processing.flatMap

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlin.system.measureTimeMillis


fun flowFrom(elem: String) = flowOf(1, 2, 3)
    .onEach {
        delay(1000)
        println("1 sec")
    }
    .map { "${it}_${elem} " }

// flatMapConcat operates on flow(outer) of flows(inner).
// It processes the produced inner flows one after another.
// So, the second inner flow can start when the first one is done.
suspend fun main() {
    println("##### FLow flat #####")
    val flowFlat: Flow<String> = flowOf("A", "B", "C")
        .flatMapConcat { flowFrom(it) } // If we had used map, result would be a Flow of Flows instead
    val timeTaken = measureTimeMillis { flowFlat.collect { println(it) } }
    println("##### Time taken: $timeTaken #####")

    println("##### FLow not flat #####")
    val flowNotFlat: Flow<Flow<String>> = flowOf("A", "B", "C")
        .map { flowFrom(it) }
    flowNotFlat.collect { println(it) }
}