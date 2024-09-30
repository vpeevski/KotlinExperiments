package com.kotlinreflection

import kotlin.reflect.*

class Box2(
    var value: Int = 0
)

fun main() {
    val box = Box2()
    val p: KMutableProperty1<Box2, Int> = Box2::value
    println(p.get(box)) // 0
    p.set(box, 999)
    println(p.get(box)) // 999
}