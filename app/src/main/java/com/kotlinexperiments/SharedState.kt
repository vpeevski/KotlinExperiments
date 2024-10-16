package com.kotlinexperiments

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicReference

suspend fun main() {
    val downloader = UserDownloader(FakeNetworkService())
    coroutineScope {
        repeat(10_000) {
            launch {
                downloader.fetchUser(it)
            }
        }
    }
    println("Users downloaded: ${downloader.downloaded().size}") // ~998242

    println("##### Single threaded dispatcher ######")
    val downloaderSingleThreaded = UserDownloaderSingleThread(FakeNetworkService())
    coroutineScope {
        repeat(10_000) {
            launch {
                downloaderSingleThreaded.fetchUser(it)
            }
        }
    }
    println("Users downloaded: ${downloaderSingleThreaded.downloaded().size}")
}

class UserDownloader(
    private val api: NetworkService
) {
    // Not thread-safe
//    fun downloaded(): List<UserData> = users.toList()
//    private val users = mutableListOf<UserData>()

    private val users = AtomicReference(listOf<UserData>())
    fun downloaded(): List<UserData> = users.get()

    suspend fun fetchUser(id: Int) {
        val newUser = api.fetchUser(id)
//         users.add(newUser)
        users.getAndUpdate { it + newUser }
    }
}

class UserDownloaderSingleThread(
    private val api: NetworkService
) {
    private val users = mutableListOf<UserData>()
    private val dispatcher = Dispatchers.IO
        .limitedParallelism(1)

    suspend fun downloaded(): List<UserData> =
        withContext(dispatcher) {
            users.toList()
        }

    suspend fun fetchUser(id: Int) {
        val newUser = api.fetchUser(id)
        withContext(dispatcher) {
            users.add(newUser)
        }
    }
}

interface NetworkService {
    suspend fun fetchUser(id: Int): UserData
}

data class UserData(val id: String)

class FakeNetworkService : NetworkService {
    override suspend fun fetchUser(id: Int): UserData {
        delayNotCancelable(2)
        return UserData("User$id")
    }
}

