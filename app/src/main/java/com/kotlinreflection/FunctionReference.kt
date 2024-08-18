package com.kotlinreflection

import kotlin.reflect.*

fun printABC() {
    println("ABC")
}

fun double(i: Int): Int = i * 2

class Complex(val real: Double, val imaginary: Double) {
    fun plus(number: Number): Complex = Complex(
        real = real + number.toDouble(),
        imaginary = imaginary
    )
}

fun Complex.double(): Complex =
    Complex(real * 2, imaginary * 2)

fun Complex?.isNullOrZero(): Boolean =
    this == null || (this.real == 0.0 && this.imaginary == 0.0)

class Box<T>(var value: T) {
    fun get(): T = value
}

fun <T> Box<T>.set(value: T) {
    this.value = value
}

fun main() {
    // Unbounded function references
    val f1 = ::printABC
    val f2 = ::double
    val f3 = Complex::plus
    val f4 = Complex::double
    val f5 = Complex?::isNullOrZero
    val f6 = Box<Int>::get
    val f7 = Box<String>::set

    // Bounded function references - without receiver as first parameter
    val c = Complex(1.0, 2.0)
    val f3Bounded: KFunction1<Number, Complex> = c::plus
    val f4Bounded: KFunction0<Complex> = c::double
    val f5Bounded: KFunction0<Boolean> = c::isNullOrZero
    val b = Box(123)
    val f6Bounded: KFunction0<Int> = b::get
    val f7Bounded: KFunction1<Int, Unit> = b::set
}