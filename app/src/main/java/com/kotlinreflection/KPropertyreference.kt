package com.kotlinreflection

import kotlin.reflect.*
import kotlin.reflect.full.memberExtensionProperties

val lock: Any = Any()
var str: String = "ABC"

class Box1(
    var value: Int = 0
) {
    val Int.addedToBox
        get() = Box1(value + this)
}

fun main() {
    val p1: KProperty0<Any> = ::lock
    println(p1) // val lock: kotlin.Any
    val p2: KMutableProperty0<String> = ::str
    println(p2) // var str: kotlin.String
    val p3: KMutableProperty1<Box1, Int> = Box1::value
    println(p3) // var Box.value: kotlin.Int
    val p4: KProperty2<Box1, *, *> = Box1::class
        .memberExtensionProperties
        .first()
    println(p4) // val Box.(kotlin.Int.)addedToBox: Box
}