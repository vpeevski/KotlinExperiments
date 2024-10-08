package com.kotlinreflection

import kotlin.reflect.full.memberProperties

fun displayPropertiesAsList(instance: Any) {
    instance::class.memberProperties
        .sortedBy { it.name }
        .map { p -> " * ${p.name}: ${p.call(instance)}" }
        .forEach(::println)
}

class Person(
    val name: String,
    val surname: String,
    val children: Int,
    val female: Boolean,
)

class Dog(
    val name: String,
    val age: Int,
)

enum class DogBreed {
    HUSKY, LABRADOR, PUG, BORDER_COLLIE
}

fun main() {
    val granny = Person("Esmeralda", "Weatherwax", 0, true)
    displayPropertiesAsList(granny)
    // * children: 0
    // * female: true
    // * name: Esmeralda
    // * surname: Weatherwax

    val cookie = Dog("Cookie", 1)
    displayPropertiesAsList(cookie)
    // * age: 1
    // * name: Cookie

    displayPropertiesAsList(DogBreed.BORDER_COLLIE)
    // * name: BORDER_COLLIE
    // * ordinal: 3
}