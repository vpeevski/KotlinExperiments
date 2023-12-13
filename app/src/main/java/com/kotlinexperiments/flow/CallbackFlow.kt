package com.kotlinexperiments.flow

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlin.concurrent.thread


suspend fun main() = coroutineScope {
    val job = launch {
        val callbackApi: CallbackApi<List<String>> = CallbackApiImpl()
        val data = getData(callbackApi)
        data.collect {
            println(it)
            cancel()
        }
    }
}

fun getData(callbackApi: CallbackApi<List<String>>): Flow<List<String>> = callbackFlow {
    val callback = object : ApiCallback<List<String>> {
        override fun onNextValue(value: List<String>) {
            // println("Next value received: $value")
            trySend(value)
        }

        override fun onApiError(cause: Throwable) {
            println("Api error: $cause")
            cancel(CancellationException("API Error", cause))
        }

        override fun onCompleted() {
            println("Api completed!")
            channel.close()
        }
    }
    callbackApi.fetchData(callback)
    // Otherwise, a callback/listener may leak in case of external cancellation.
    awaitClose { callback.onCompleted() } // Will throw IllegalStateException without this line.
}


interface ApiCallback<T> {
    fun onNextValue(value: T)
    fun onApiError(cause: Throwable)
    fun onCompleted()
}

interface CallbackApi<T> {
    fun fetchData(callback: ApiCallback<T>)
}

class CallbackApiImpl : CallbackApi<List<String>> {
    override fun fetchData(callback: ApiCallback<List<String>>) {
        thread {
            Thread.sleep(2000)
            callback.onNextValue(List(3) { "Item${it + 1}" })
        }
    }
}