package com.progress

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay

suspend fun main(): Unit = coroutineScope {
    simulateProgress { progress ->
        println("Progress: ${(progress * 100).toInt()}%")
    }
}

suspend fun simulateProgress(
    stepsCount: Int = 5,
    stepInterval: Long = 200L,
    publish: (Float) -> Unit,
) {
    require(stepsCount >= 2) { "stepsCount must be at least 2 to reserve the last step for real completion." }

    val totalSteps = stepsCount - 1
    val stepWeight = 1f / stepsCount
    var current = 0f

    repeat(totalSteps) {
        delay(stepInterval)
        current += stepWeight
        publish(current)
    }
}