package com.kotlinexperiments.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

suspend fun main() {
    val api = FakeUserApi()
    // val users = allUsersFlow(api)
    val users = allUsersChannelFlow(api)
    val user = users.first {
        println("Checking $it")
        delay(1000) // suspending
        it.name == "User3"
    }
    println(user)
}

// Next page is requested lazily when it is needed
fun allUsersFlow(api: UserApi): Flow<User> = flow {
    var page = 0
    do {
        println("Fetching page $page")
        val users = api.takePage(page++) // suspending
        emitAll(users.asFlow())
    } while (!users.isNullOrEmpty())
}

// Once it is started, it produces the values in a separate coroutine
// without waiting for the receiver. Therefore, fetching the next pages
// and checking users happens concurrently.
fun allUsersChannelFlow(api: UserApi): Flow<User> = channelFlow {
    var page = 0
    do {
        println("Fetching page $page")
        val users = api.takePage(page++) // suspending
        users?.forEach { send(it) }
    } while (!users.isNullOrEmpty())
}

data class User(val name: String)
interface UserApi {
    suspend fun takePage(pageNumber: Int): List<User>
}

class FakeUserApi : UserApi {
    private val users = List(20) { User("User$it") }
    private val pageSize: Int = 3
    override suspend fun takePage(
        pageNumber: Int
    ): List<User> {
        delay(1000) // suspending
        return users
            .drop(pageSize * pageNumber)
            .take(pageSize)
    }
}
