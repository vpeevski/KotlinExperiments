package com.kotlinexperiments.channels

import kotlin.system.measureTimeMillis

class PrimeNumberIterator : Iterator<Int> {
    private var current = 2

    override fun hasNext(): Boolean {
        return true // The iterator generates prime numbers indefinitely
    }

    override fun next(): Int {
        val nextPrime = current
        current = generateNextPrime(current + 1)
        return nextPrime
    }

    private fun isPrime(number: Int): Boolean {
        if (number < 2) return false
        for (i in 2 until number) {
            if (number % i == 0) {
                return false
            }
        }
        return true
    }

    private fun generateNextPrime(start: Int): Int {
        var candidate = start
        while (!isPrime(candidate)) {
            candidate++
        }
        return candidate
    }
}

fun main() {
    val primeNumberIterator = PrimeNumberIterator()

    // Print the first 10 prime numbers
    val timeTaken = measureTimeMillis {
        repeat(100_000) {
            println(primeNumberIterator.next())
        }
    }
    println("Time taken: $timeTaken")

}