package com.kotlinexperiments

import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.random.Random

suspend fun main() {
    println("Before")
    suspendCoroutine<Unit> { continuation ->
        thread {
            println("Suspended")
            Thread.sleep(2000)
            continuation.resume(Unit)
            println("Resumed")
        }

    }
    delay(1000)
    val user1 = requestUserRepo()
    println("### Local User: $user1")
    val result = runCatching { requestUserRepo(success = false) }
    println("### Result: $result")
    println("After")
}

val executor = Executors.newSingleThreadScheduledExecutor {
    Thread(it, "scheduler").apply {
        isDaemon = true
    }
}

suspend fun delay(timeMillis: Long) = suspendCoroutine { continuation ->
//    println("Suspended")
    executor.schedule({
        continuation.resume(Unit)
    }, timeMillis, TimeUnit.MILLISECONDS)
//    println("Resumed")
}

suspend fun delayCancelable(timeMillis: Long) = suspendCancellableCoroutine { continuation ->
//    println("Suspended")
    executor.schedule({
        continuation.resume(Unit)
    }, timeMillis, TimeUnit.MILLISECONDS)
//    println("Resumed")
}

suspend fun requestUserRepo(success: Boolean = true): ApiResponse<Uzer> {
    return suspendCancellableCoroutine { cont ->
        requestUserWithCallback(responseCallback = { apiResponse ->
            println(apiResponse)
            when (apiResponse) {
                is ApiResponse.Success -> cont.resume(apiResponse)
                is ApiResponse.Error -> cont.resumeWithException(apiResponse.exception)
            }
        }, succeed = success)
    };
}

fun requestUserBlocking(): Uzer {
    Thread.sleep(2000)
    return Uzer(id = Random(1000).nextInt(), name = "Ivan")
}

fun requestUserWithCallback(responseCallback: (ApiResponse<Uzer>) -> Unit, succeed: Boolean) {
    Thread.sleep(2000)
    return if (succeed)
        responseCallback(ApiResponse.Success(Uzer(id = Random(1000).nextInt(), name = "Ivan")))
    else responseCallback(ApiResponse.Error(java.lang.Exception("Api Error")))
}

data class Uzer(val id: Number, val name: String)

sealed class ApiResponse<out T> {
    data class Success<R>(val data: R) : ApiResponse<R>()
    data class Error(val exception: Exception) : ApiResponse<Nothing>()
}