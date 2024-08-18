package com.kotlinreflection

import kotlin.reflect.KCallable

fun String.times(count: Int) = this.repeat(count)

fun main() {
    // Unbound
    val callable: KCallable<String> = String::times
    println(callable.call("Hello", 3))

    val sumCallable = Int::sum
    println(sumCallable(5, 6))
    println(5.sum(6))
    val sumFun: (Int, Int) -> Int = Int::sum
    println(sumFun(5, 6))
}

fun Int.sum(a: Int): Int = this + a