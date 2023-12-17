package com.kotlinexperiments.flow.hot

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

// StateFlow is an extension of the SharedFlow concept. It works similarly
// to SharedFlow when the replay parameter is set to 1.
// It always stores one value, which can be accessed using the value property.

suspend fun main() = coroutineScope {
    val state = MutableStateFlow("A")
    println(state.value) // A
    launch {
        state.collect { println("#1 Value changed to $it") }
    }
    delay(1000)
    state.value = "B" // Value changed to B
    delay(1000)
    launch {
        state.collect { println("#2 and now it is $it") }
// and now it is B
    }
    delay(1000)
    state.value = "C" // Value changed to C and now it is C
}
// On Android, StateFlow is used as a modern alternative to LiveData.
// First, it has full support for coroutines. Second, it has an initial value,
// so it does not need to be nullable. So, StateFlow is often used on
// ViewModels to represent its state. This state is observed, and a view
// is displayed and updated on this basis.