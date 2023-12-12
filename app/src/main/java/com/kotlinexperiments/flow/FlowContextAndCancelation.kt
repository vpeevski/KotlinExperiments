package com.kotlinexperiments.flow

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Notice, that this function is not suspending
// and does not need CoroutineScope.
fun usersFlow(): Flow<String> = flow {
    repeat(3) {
        val ctx = currentCoroutineContext()
        val name = ctx[CoroutineName]?.name
        emit("User$it in $name")
        delay(2000)
    }
}

suspend fun main() {
    val users = usersFlow()
    withContext(CoroutineName("MyCoroutine")) {
        val job = launch {
            // collect is suspending
            users.collect { println(it) }
        }

        launch {
            delay(2100)
            println("I got enough, cancel flow collecting !")
            job.cancel()
        }
        job.join()
    }
}