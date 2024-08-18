package com.delegation

import kotlin.reflect.KProperty

private class LoggingProperty<T>(var value: T) {
    operator fun getValue(thisRef: Any?, prop: KProperty<*>): T {
        println("${prop.name} in $thisRef getter returned $value")
        return value
    }

    operator fun setValue(
        thisRef: Any?,
        prop: KProperty<*>,
        newValue: T
    ) {
        println("${prop.name} in $thisRef changed from $value to $newValue")
        value = newValue
    }
}


var token: String? by LoggingProperty(null)
var attempts: Int by LoggingProperty(0)

object AttemptsCounter {
    var attempts: Int by LoggingProperty(0)
}

fun main() {
    token = "AAA" // token changed from null to AAA
    val res = token // token getter returned AAA
    println(res) // AAA
    attempts++
    // attempts getter returned 0
    // attempts changed from 0 to 1

    AttemptsCounter.attempts = 1
    // attempts in AttemptsCounter@XYZ changed from 0 to 1
    val res2 = AttemptsCounter.attempts
    // attempts in AttemptsCounter@XYZ getter returned 1
    println(res2) // 1
}