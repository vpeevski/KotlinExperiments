package com.kotlinexperiments

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

suspend fun main(): Unit = coroutineScope {
//    val ctx = CoroutineName("Name1") + Job()
//    ctx.fold("") { acc, element -> "$acc$element " }.also(::println)
//    // CoroutineName(Name1) JobImpl{Active}@dbab622e
//    val empty = emptyList<CoroutineContext>()
//    ctx.fold(empty) { acc, element -> acc + element }
//        .joinToString()
//        .also(::println)
//    // CoroutineName(Name1), JobImpl{Active}@dbab622e

    // Children inherit CoroutineContext from parent
    println("###")
    println("### Children inherit CoroutineContext from parent: ")
    withContext(CoroutineName("Parent")) {
        myLog("Started") // [main] Started
        val v1 = async {
            delayNotCancelable(500)
            myLog("Running async") // [Parent]: Running async
            42
        }
        launch {
            delayNotCancelable(1000)
            myLog("Running launch") // [Parent]: Running launch
        }
        // Child can override parent context.
        // Simplified formula: defaultContext + parentContext + childContext
        // Since new elements always replace old ones with the same key,
        // the child context always overrides elements
        // with the same key from the parent context.

        delayNotCancelable(2000)
        println("###")
        println("### Child can override parent context.: ")
        launch(CoroutineName("Child Context")) {
            delayNotCancelable(500)
            myLog("Second launch running") // [Child Context]: Second launch running
        }
        myLog("The answer is ${v1.await()}")
        delayNotCancelable(1000)

        // Accessing context in a suspending function
        println("###")
        println("### Accessing context in a suspending function: ")
        withContext(CoroutineName("Outer")) {
            delayNotCancelable(500)
            printName()
            launch(CoroutineName("Inner")) {
                delayNotCancelable(1000)
                printName()
            }

        }
        delayNotCancelable(2000)
        println("###")
    }
}

fun CoroutineScope.myLog(msg: String) {
    val name = coroutineContext[CoroutineName]?.name
    println("Coroutine Context [$name]: $msg")
}

// Accessing context in a suspending function
suspend fun printName() {
    println("From suspend function CoroutineContext: ${coroutineContext[CoroutineName]?.name}")
}