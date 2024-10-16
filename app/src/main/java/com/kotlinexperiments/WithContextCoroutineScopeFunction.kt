package com.kotlinexperiments

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

// The withContext function is similar to coroutineScope, but it
// additionally allows some changes to be made to the scope. The
// context provided as an argument to this function overrides the
// context from the parent scope (the same way as in coroutine
// builders). This means that withContext(EmptyCoroutineContext)
// and coroutineScope() behave in exactly the same way.
fun CoroutineScope.log(text: String) {
    val name = this.coroutineContext[CoroutineName]?.name
    println("[$name] $text")
}

fun main() = runBlocking(CoroutineName("Parent")) {
    log("Before")
//    The function withContext is often used to set a different coroutine
//    scope for part of our code. Usually, you should use it together with
//    dispatchers.
    withContext(CoroutineName("Child 1")) {
        delayNotCancelable(1000)
        log("Hello 1")
    }
    withContext(CoroutineName("Child 2")) {
        delayNotCancelable(1000)
        log("Hello 2")
    }
    log("After")
}