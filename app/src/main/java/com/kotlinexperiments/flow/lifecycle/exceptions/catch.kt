package com.kotlinexperiments.flow.lifecycle.exceptions

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach

// At any point of flow building or processing, an exception might
// occur. Such an exception will flow down, closing each processing
// step on the way; however, it can be caught and managed. To do so,
// we can use the catch method. This listener receives the exception as
// an argument and allows you to perform recovering operations.
class MyError : Throwable("My error")

val data = flow {
    emit(1)
    emit(2)
    throw MyError()
}

suspend fun main() {
    // In the example below, notice that onEach does not react
    // to an exception. The same happens with other functions
    // like map, filter etc. Only the onCompletion handler will
    // be called.
    data.onEach {
        delay(1000)
        println("Got $it")
    }.catch {
        println("Caught $it")
        emit(-1)
    }.collect { println("Collected $it") }
    // Note 1: The catch method stops an exception by catching it.
    // The previous steps have already been completed,
    // but catch can still emit new values and keep the
    // rest of the flow alive.

    // Note 2: The catch will only react to the exceptions thrown in the function
    // defined upstream (you can imagine that the exception needs to be caught as it flows down).

    println("##### Catch handles ONLY exceptions thrown by functions defined upstream!!! #####")
    val data1 = flow {
        emit("Message1")
        throw MyError()
    }
    data1.catch {
        emit("Error")
    }.onEach {
        throw MyError() // Not caught by catch block.
    }.collect {
        println("Collected: $it")
    }

    // NOTE 3: In Android, we often use catch to show exceptions that happened in a flow.
    // Also to emit default data to display on the screen, such as an empty list.
//    fun updateNews() {
//        scope.launch {
//            newsFlow()
//                .catch {
//                    view.handleError(it)
//                    emit(emptyList())
//                }
//                .onStart { showProgressBar() }
//                .onCompletion { hideProgressBar() }
//                .collect { view.showNews(it) }
//        }
//    }

}