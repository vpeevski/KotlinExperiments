package com.kotlinexperiments.flow.processing.flatMap

import kotlinx.coroutines.flow.flowOf

// flatMap is NOT available for Flow, only for collections!
// For collections it is similar to a map, but the transformation function
// needs to return a collection that is then flattened.

// How should flatMap look on a flow? - there is no clear answer.
// It seems intuitive that we might expect its transformation function
// to return a flow that should then be flattened.
// The problem is that flow elements can be spread in
// time. So, should the flow produced from the second element wait
// for the one produced from the first one, or should it process them
// concurrently? Since there is no clear answer, there is no flatMap
// function for Flow, but instead there are flatMapConcat, flatMapMerge
// and flatMapLatest.
fun main() {
    println("##### List flat #####")
    val listFlat = listOf(1, 2, 3)
        .flatMap { // If we had used map, result would be a List of List<Int> instead of Lit<Int>
            clonning(it, it) // [1, 2, 2, 3, 3, 3]
        }
    println(listFlat)

    println("##### List NOT flat #####")
    val listNotFlat = listOf(1, 2, 3)
        .map {
            clonning(it, it) // [[1], [2, 2], [3, 3, 3]]
        }
    println(listNotFlat)
}

fun clonning(num: Int, times: Int): List<Int> = List(times) { num }