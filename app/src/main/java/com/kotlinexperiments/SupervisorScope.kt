package com.kotlinexperiments

import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext

// SupervisorScope is mainly used in functions that start multiple independent tasks.
// The supervisorScope function also behaves a lot like coroutineScope:
// it creates a CoroutineScope that inherits from the outer scope and
// calls the specified suspend block in it. The difference is that it
// overrides the context’s Job with SupervisorJob, so it is not cancelled
// when a child raises an exception.
suspend fun main(): Unit = coroutineScope {
    supervisorScope {
        launch {
            delayNotCancelable(1000)
            throw Error("Some error")
        }
        launch {
            delayNotCancelable(2000)
            println("Will be printed")
        }
    }
    delayNotCancelable(1000)
    println("Done")

    println("##### Use supervisorScope #####")
    notifyAnalytics(listOf("A1", "A2", "A3"))
    println("##### DO NOT DO THIS #####")
    sendNotifications(listOf("A1", "A2", "A3"))
    println("Will be NOT PRINTED !!!")
}

suspend fun notifyAnalytics(actions: List<String>) = supervisorScope {
    actions.forEach { action ->
        launch {
            notifyAnalytics(action)
        }
    }
}

suspend fun notifyAnalytics(action: String) {
    if (action == "A2") throw RuntimeException("Invalid action")
    delayCancelable(1000)
    println(action)
}

// DON'T DO THAT!
// supervisorScope cannot be replaced with withContext(SupervisorJob())
suspend fun sendNotifications(
    notifications: List<String>
) =
    withContext(SupervisorJob()) { // Job is the only context that is not inherited. Here SupervisorJob is a parent of withContext coroutine.
        for (notification in notifications) {
            launch {
                notifyAnalytics(notification)
            }
        }
    }