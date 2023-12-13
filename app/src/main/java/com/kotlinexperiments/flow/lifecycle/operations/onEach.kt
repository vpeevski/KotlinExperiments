package com.kotlinexperiments.flow.lifecycle.operations

import com.kotlinexperiments.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach

// They all flow, we can listen for values, exceptions,
// or other characteristic events (like starting or completing). To do
// this, we use methods such as onEach, onStart, onCompletion, onEmpty
// and catch.
suspend fun main() {
    // To react to each flowing value, we use the onEach function.
    flowOf(1, 2, 3, 4)
        .onEach {
            delay(1000)
            print(it)
        } // onEach lambda expression is suspending, and elements are processed one after another in order (sequentially).
        .collect {} // 1234
}