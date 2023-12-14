package com.kotlinexperiments.flow.processing

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

// Most flow processing and lifecycle functions
// can be implemented with
// flow builder and collect with a lambda.
suspend fun main() {
    flowOf('a', 'b')
        .map { it.uppercase() }
        .collect { print(it) } // AB
    println()
    println("##### Inline functions #####")
    inlineFunctions()
}

fun <T, R> Flow<T>.map(
    transform: suspend (value: T) -> R
): Flow<R> = flow {
    collect { value ->
        emit(transform(value))
    }
}

fun <T> flowOf(vararg elements: T): Flow<T> = flow {
    for (element in elements) {
        emit(element)
    }
}

// We start a flow at 1 and collect it at 7.
// When we start collecting, we invoke the lambda @map (which starts at 1),
// which calls another builder at 2 and collects it at 5.
// When we collect, we start lambda @flowOf (which starts at 2). This
// lambda (at 2) iterates over an array with 'a' and 'b'. The first value
// 'a' is emitted at 4, which calls the lambda at 5. This lambda (at 5)
// transforms the value to 'A' and emits it to the flow @map, thus
// calling the lambda at 7. The value is printed; we then finish the
// lambda at 7 and resume the lambda at 6. It finishes, so we resume
// @flowOf at 4. We continue the iteration and emit 'b' at 4. So, we call
// the lambda at 5, transform the value to 'B', and emit it at 6 to the
// flow @map. The value is collected at 7 and printed at 8. The lambda
// at 7 is finished, so we resume the lambda at 6. This is finished, so we
// resume the lambda @flowOf at 4. This is also finished, so we resume
// the @map on collect at 5. Since
// there is nothing more, we reach the end of @map. With that, we
// resume the collect at 7, and we reach the end of the main function.
suspend fun inlineFunctions() {
    flow map@{ // 1
        flow flowOf@{ // 2
            for (element in arrayOf('a', 'b')) { // 3
                this@flowOf.emit(element) // 4
            }
        }.collect { value -> // 5
            this@map.emit(value.uppercase()) // 6
        }
    }.collect { // 7
        print(it) // 8
    }
}