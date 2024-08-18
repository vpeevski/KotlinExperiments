package com.delegation

fun main() {
    val map: Map<String, Any> = mapOf(
        "name" to "Marcin",
        "kotlinProgrammer" to true
    )
    val name: String by map
    val kotlinProgrammer: Boolean by map
    println(name) // Marcin
    println(kotlinProgrammer) // true
}