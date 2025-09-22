package com.interviewtasks

fun main() {
    var a = 5
    var b = 6
    println("a: $a, b: $b")
    a = b.also { b = a }
    println("a: $a, b: $b")
}