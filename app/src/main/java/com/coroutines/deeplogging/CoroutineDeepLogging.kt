package com.coroutines.deeplogging

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

private suspend inline fun runWithLogging(
    action: () -> Unit,
) {
    try {
        action()
        logWithCoroutineState(
            suffix = "Completed successfully",
            coroutineState = { coroutineContext.coroutineState() },
            tag = "",
        )
    } catch (e: Exception) {
        logWithCoroutineState(
            suffix = "Failed with exception",
            loggerFunction = { _, _, _ -> },
            error = e,
            coroutineState = { coroutineContext.coroutineState() },
            tag = "",
        )
        throw e
    }
}

private inline fun logWithCoroutineState(
    prefix: String? = null,
    suffix: String? = null,
    error: Throwable? = null,
    tag: String,
    loggerFunction: (tag: String, message: String, t: Throwable?) -> Unit = { _, _, _ -> },
    coroutineState: () -> String,
) {
    val message = listOfNotNull(
        prefix,
        coroutineState(),
        suffix,
    ).joinToString(separator = " - ")
    if (message.isNotEmpty() || error != null) {
        loggerFunction(tag, message, error)
    }
}

private fun CoroutineContext.coroutineState(): String =
    "[Coroutine: Name: ${this[CoroutineName]?.name}, ${this[Job]?.jobState()}]"

private fun Job.jobState(): String {
    val state = when {
        isActive -> "Active"
        isCompleted -> "Completed"
        isCancelled -> "Cancelled"
        else -> "Job state is unknown"
    }
    return "State: $state, Job: ${hashCode()} ${
        parent?.jobState()?.let { ", Parent: [$it]" } ?: ""
    }"
}