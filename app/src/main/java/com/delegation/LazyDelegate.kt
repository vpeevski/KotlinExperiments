package com.delegation

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay

data class User(
    val name: String,
    val surname: String,
    val pronouns: String,
    val gender: String,
    // ...
) {
    val fullDisplay: String by lazy { produceFullDisplay() }

    fun produceFullDisplay(): String {
        println("Calculating...")
        Thread.sleep(2000)
        // ...
        return "Calculated !!!"
    }
}

fun test() {
    val user = User(
        name = "Ivan",
        surname = "Ivanov",
        pronouns = "Ivanche",
        gender = "Male",

        )
    val copy = user.copy()
    println(copy.fullDisplay) // Calculating... XYZ
    println(copy.fullDisplay) // XYZ
}

fun main() {
    test()
}