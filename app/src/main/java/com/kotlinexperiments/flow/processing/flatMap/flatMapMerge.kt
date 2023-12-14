package com.kotlinexperiments.flow.processing.flatMap

import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlin.system.measureTimeMillis

// flatMapMerge, is the most intuitive, it processes produced flows concurrently.
suspend fun main() {
    println("##### Concurrency default (16) #####")
    val timeTaken = measureTimeMillis {
        flowOf("A", "B", "C")
            .flatMapMerge { flowFrom(it) }
            .collect { println(it) }
    }
    println("##### Default concurrency (16). Time taken: $timeTaken #####")

    // The number of flows that can be concurrently processed can be set
    // using the concurrency parameter. The default value of this parameter is 16,
    // but it can be changed in the JVM using the DEFAULT_CONCURRENCY_PROPERTY_NAME property.
    // Beware of this default limitation because if you use flatMapMerge on a flow
    // with many elements, only 16 will be processed at the same time.
    println("##### Concurrency limited to 2 #####")
    val timeTakenLimited = measureTimeMillis {
        flowOf("A", "B", "C")
            .flatMapMerge(concurrency = 2) { flowFrom(it) }
            .collect { println(it) }
    }
    println("##### Limited concurrency (2). Time taken: $timeTakenLimited #####")
}

// The typical use of flatMapMerge is when we need to request data for
// each element in a flow. For instance, we have a list of categories, and
// you need to request offers for each of them. You already know that
// you can do this with the async function. There are two advantages of
// using a flow with flatMapMerge instead:
// • 1. we can control the concurrency parameter and decide how
//      many categories we want to fetch at the same time (to avoid
//      sending hundreds of requests at the same time);
//•  2. we can return Flow and send the next elements as they arrive
//      (so, on the function-use side, they can be handled immediately).

// suspend fun getOffers(
//     categories: List<Category> ): List<Offer> = coroutineScope {
//    categories
//        .map { async { api.requestOffers(it) } }
//        .flatMap { it.await() }
// }

// // A better solution
// suspend fun getOffers(
//     categories: List<Category>): Flow<Offer> = categories
//     .asFlow()
//     .flatMapMerge(concurrency = 20) {
//         suspend { api.requestOffers(it) }.asFlow()
// // or flow { emit(api.requestOffers(it)) }
// }