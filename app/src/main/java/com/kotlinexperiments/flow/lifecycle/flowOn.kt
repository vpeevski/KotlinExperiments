package com.kotlinexperiments.flow.lifecycle

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

// Lambda expressions used as arguments for flow operations (like
// onEach, onStart, onCompletion, etc.) and its builders (like flow or
// channelFlow) are all suspending in nature. Suspending functions
// need to have a context and should be in relation to their parent (for
// structured concurrency). So, you might be wondering where these
// functions take their context from. The answer is: from the context
// where collect is called.
suspend fun present(caller: String, message: String) {
    println("[${contextName()}] $caller: $message")
}

suspend fun contextName(): String? {
    val ctx = currentCoroutineContext()
    return ctx[CoroutineName]?.name
}

fun usersFlow(): Flow<String> = flow {
    println(present("flow builder", "block"))
    repeat(2) {
        emit("User$it in ${contextName()}")
    }
}

suspend fun main() {
    println("##### flowOn #####")
    val users = usersFlow()
    withContext(CoroutineName("Name1")) {
        users.collect { println(it) }
    }
    withContext(CoroutineName("Name2")) {
        users.collect { println(it) }
    }
    // The terminal operation call requests elements from upstream,
    // thereby propagating the coroutine context.
    // However, it can also be modified by the flowOn function.
    println("##### flowOn #####")
    withContext(CoroutineName("Name3")) {
        users
            .flowOn(CoroutineName("Name4")) // Affects flow builder
            .onEach { present("onEach", it) } // Name5
            .flowOn(CoroutineName("Name5")) // Affects upper onEach
            .collect { present("collect", it) } // Not affected: Name3

    }
    // NOTE: Remember that flowOn works only for functions that are upstream in the flow.
}