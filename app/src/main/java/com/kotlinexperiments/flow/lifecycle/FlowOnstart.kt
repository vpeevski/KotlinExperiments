package com.kotlinexperiments.flow.lifecycle

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

// The onStart function sets a listener that should be called immediately
// once the flow is started, i.e., once the terminal operation is
// called. It is important to note that onStart does not wait for the first
// element: it is called when we request the first element.
suspend fun main() {
    flowOf(1, 2)
        .onEach {
            delay(1000)
            println("1 sec.")
        }
        .onStart { println("Before") }
        .collect { println(it) }

    // It is good to know that in onStart we can emit elements.
    // As well as in onCompletion, onEmpty and catch.
    // Such elements will flow downstream from this place.
    println("#### onStart emits ####")
    flowOf(1, 2)
        .onEach { delay(1000) }
        .onStart { emit(0) }
        .collect { println(it) } // 012
}