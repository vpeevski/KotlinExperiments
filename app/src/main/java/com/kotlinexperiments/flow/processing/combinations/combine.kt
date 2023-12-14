package com.kotlinexperiments.flow.processing.combinations

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

// combine just like zip also forms pairs from elements, which have to
// wait for the slower flow to produce the first pair. However, the
// similarities to the polonaise dance end here. When we use combine,
// every new element replaces its predecessor. If the first pair has been
// formed already, it will produce a new pair together with the previous
// element from the other flow.
// Some elements might NOT ever be able to form pair - like A.
suspend fun main() {
    val flow1 = flowOf("A", "B", "C")
        .onCompletion { println("Flow1 Completed") }
        .onEach { delay(400) }
    val flow2 = flowOf(1, 2, 3, 4)
        .onCompletion { println("Flow2 Completed") }
        .onEach { delay(1000) }
    flow1.combine(flow2) { f1, f2 -> "${f1}_${f2}" }
        .onStart { emit("Z_100") }
        .onCompletion { println("Combine Completed") }
        .collect { println(it) }
}
// Notice that zip needs pairs, so it closes when one of the flows closes.
// combine does not have such a limitation, so it will emit until all
// flows are closed.

// combine is typically used when we need to actively observe two
// sources of changes. If you want to have elements emitted whenever
// a change occurs.
// You can add initial values to each combined flow (to have the initial pair).

// A typical use case might be when a view needs to be updated when
// either of two observable element changes.
// For example, when a notification badge depends on both the current
// state of a user and some notifications, we might observe them both
// and combine their changes to update a view.
// userStateFlow
// .combine(notificationsFlow) { userState, notifications ->
//     updateNotificationBadge(userState, notifications)
// }
// .collect()