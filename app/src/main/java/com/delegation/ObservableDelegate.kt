package com.delegation

import kotlin.properties.Delegates.observable

var name: String by observable("Empty") { prop, old, new ->
    println("Set property ${prop.name}: $old -> $new")
}

fun main() {
    name = "Martin" // Empty -> Martin
    name = "Igor" // Martin -> Igor
    name = "Igor" // Igor -> Igor
}