package com.kotlinexperiments.flow.processing.flatMap

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach

// flatMapLatest forgets about the previous flow once a new one appears.
// With every new value, the previous flow processing is forgotten.
suspend fun main() {
    println("##### Without outer flow delay #####")
    flowOf("A", "B", "C")
        .flatMapLatest { flowFrom(it) }
        .collect { print("$it ") } // 1_C 2_C 3_C


    println()

    // It gets more interesting when the elements from the initial flow
    // are delayed. What happens in the example below is that (after 1.2 sec)
    // “A” starts its flow, which was created using flowFrom. This flow
    // produces an element “1_A” in 1 second, but 200 ms later “B” appears
    // and this previous flow is CLOSED and forgotten.
    // “B” flow managed to produce “1_B” when “C” appeared and started producing its flow.
    // This one will finally produce elements “1_C”, “2_C”, and “3_C”, with
    // a 1-second delay in between.
    println("##### With outer flow delay #####")
    flowOf("A", "B", "C")
        .onEach { delay(1200) }
        .flatMapLatest { flowFrom(it) }
        .collect { print("$it ") } // 1_A 1_B 1_C 2_C 3_C
}