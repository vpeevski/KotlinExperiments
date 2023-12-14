package com.kotlinexperiments.flow.processing.aggregation

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.onEach

// fold is a terminal operation. It can be used for Flow and Collections.
// When used with Flow it will suspend until this flow is completed (just like collect).

// fold is used to combine all the values in this collection into one by
// applying an operation that combines two values into one for each
// element (starting from the initial value).

// For example, if the initial value is 0 and the operation is addition,
// then the result is the sum of all the numbers: we first take the initial
// value 0; then, we add the first element 1 to it; to the result 1, we add
// the second number 2; to the result 3, we add the third number 3; to
// the result 6, we add the last number 4. The result of this operation,
// 10, is what will be returned from fold.
suspend fun main() {
    val list = flowOf(1, 2, 3, 4)
        .onEach { delay(500) }
    val sum = list.fold(0) { sum, next ->
        sum + next
    }
    println("Sum is: $sum")

    val product = list.fold(1) { product, next ->
        product * next
    }
    println("Product is: $product")
}