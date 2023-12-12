package com.kotlinexperiments.channels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlin.system.measureTimeMillis

fun CoroutineScope.allNumbersFrom(start: Int) = produce {
    var x = start
    while (true) {
        // println("Number sent: $x")
        send(x++)
    } // infinite stream of Int starting from start
}

suspend fun CoroutineScope.print(numbers: ReceiveChannel<Int>) = numbers.consumeEach { number ->
    println(number)
}

fun CoroutineScope.filterPrimeNumbers(numbers: ReceiveChannel<Int>, prime: Int) = produce {
    for (number in numbers) if (number % prime != 0) send(number)
}

suspend fun main(): Unit = coroutineScope {
//    allNumbersFrom(2).consumeEach { number ->
//        println(number)
//    }
//    val numbers = allNumbersFrom(2)
//    print(numbers)
    firstPrimes(100_000)
}

suspend fun firstPrimes(count: Int) = coroutineScope {
    var filteredNumbers = allNumbersFrom(2)
    var primeIndex = 1
    val timeTaken = measureTimeMillis {
        repeat(count) {
            val prime = filteredNumbers.receive()
            println("Ops $prime is prime, index ${primeIndex++}")
            filteredNumbers = filterPrimeNumbers(filteredNumbers, prime)
        }
    }
    println("Time taken: $timeTaken")
    coroutineContext.cancelChildren()
}