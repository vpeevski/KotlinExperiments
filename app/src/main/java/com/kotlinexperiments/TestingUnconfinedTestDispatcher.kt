package com.kotlinexperiments

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher

// StandardTestDispatcher does not invoke any operations until we
// use its scheduler. UnconfinedTestDispatcher immediately invokes
// all the operations before the first delay on started coroutines, which
// is why the code below prints “C”
fun main() {
    val standardTestDispatcher = StandardTestDispatcher()
    CoroutineScope(standardTestDispatcher).launch {
        print("A")
        delay(1)
        print("B")
    }
//    standardTestDispatcher.scheduler.runCurrent() // To print A
//    standardTestDispatcher.scheduler.advanceTimeBy(1)
//    standardTestDispatcher.scheduler.runCurrent() // To print B

    CoroutineScope(UnconfinedTestDispatcher()).launch {
        print("C")
        delay(1)
        print("D")
    }
}