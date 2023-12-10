package com.kotlinexperiments

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.coroutines.yield

suspend fun fetchUser(): User {
// Runs forever
    while (true) {
        yield()
    }
}

// A less aggressive variant of withTimeout is withTimeoutOrNull, which
// does not throw an exception. If the timeout is exceeded, it just
// cancels its body and returns null.
suspend fun getUserOrNull(): User? = withTimeoutOrNull(2000) {
    fetchUser()
}

suspend fun main(): Unit = coroutineScope {
    val user = getUserOrNull()
    println("User: $user")
}