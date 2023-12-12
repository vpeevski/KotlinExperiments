package com.kotlinexperiments.flow

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.asFlux
import kotlinx.coroutines.rx3.asFlow
import kotlinx.coroutines.rx3.asFlowable
import kotlinx.coroutines.rx3.asObservable
import reactor.core.publisher.Flux

suspend fun main() {
    println("###########################")
    println("####### Raw values ########")
    println("###########################")
    flowOf(1, 2, 3, 4, 5).collect { print(it) }
    println()
    emptyFlow<Int>().collect { print(it) } // Noop

    // We can also convert every Iterable, Iterator or Sequence
    // into a Flow using the asFlow function.
    println("###########################")
    println("####### Converters ########")
    println("###########################")
    listOf(1, 2, 3, 4, 5).asFlow().collect { print(it) }
    println()

    // Convert a function into a Flow
    println("##################################################")
    println("####### Convert a function into a Flow ###########")
    println("##################################################")
    val function = suspend {
        // this is suspending lambda expression
        delay(1000)
        "Some Text"
    }
    println("####### Convert Function (suspending lambda expression) into a Flow ########")
    function.asFlow().collect { println(it) }

    // To convert a regular function, we need to reference it first.
    // We do this using :: in Kotlin.
    println("####### Convert Function (regular) into a Flow ########")
    ::getUserName.asFlow().collect { println(it) }

    // ####### Reactive streams ###########
    println("####################################")
    println("####### Reactive streams ###########")
    println("####################################")

    println("####### From Flux ###########")
    Flux.range(1, 5).asFlow().collect { print(it) } // 12345

    println()
    println("####### From Flowable ###########")
    Flowable.range(1, 5).asFlow().collect { print(it) } // 12345

    println()
    println("####### From Observable ###########")
    Observable.range(1, 5).asFlow().collect { print(it) } // 12345

    // Other way round
    coroutineScope {
        val flow = flowOf(1, 2, 3, 4, 5)

        println()
        println("####### To Flux ###########")
        flow.asFlux().doOnNext { print(it) } // 12345
            .subscribe()

        println()
        println("####### To Flowable ###########")
        flow.asFlowable().subscribe { print(it) } // 12345

        println()
        println("####### To Observable ###########")
        flow.asObservable().subscribe { print(it) } // 12345
    }

    println()
    println("#################################")
    println("####### Flow Builders ###########")
    println("#################################")
    // The flow builder is the most basic way to create a flow.
    // All other options are based on it.
    val makeFlow = {
        flow { // 1 block called in collect method of created Flow object.
            // Its receiver is the FlowCollector object, which is defined at 2 with a lambda expression.
            repeat(3) {
                delay(1000)
                emit(it) // Calls FlowCollector's emit method defined by lambda in collect method.
            }
        }
    }
    // Calls block function (lambda expression defined at 1) on the FlowCollector interface
    makeFlow().collect { // Builds a FlowCollector (functional interface) object with emit defined by lambda expression.
        print(it)
    }
}

suspend fun getUserName(): String {
    delay(1000)
    return "UserName"
}