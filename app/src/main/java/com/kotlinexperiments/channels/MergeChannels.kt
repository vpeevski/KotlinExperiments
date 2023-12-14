package com.kotlinexperiments.channels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.system.measureTimeMillis
import kotlin.time.ExperimentalTime

suspend fun main(): Unit = coroutineScope {
    val ordersChannel = produce {
        send(Order("1", "Mocka", "Customer1"))
        send(Order("2", "Arabica", "Customer2"))
        send(Order("3", "Nova Brazilia", "Customer3"))
        send(Order("4", "", "Customer2"))
        send(Order("5", "Lavazza", "Customer3"))
    }

    val coffeeResultChannel = merge(
        serveOrders(ordersChannel, "Alex"),
        serveOrders(ordersChannel, "Bob"),
        serveOrders(ordersChannel, "Celine")
    )

    for (coffeeResult in coffeeResultChannel) {
        println("Coffee: $coffeeResult")
    }
}

suspend fun CoroutineScope.serveOrders(
    orders: ReceiveChannel<Order>,
    baristaName: String
): ReceiveChannel<CoffeeResult> = produce {
    for (order in orders) {
        val coffee = prepareCoffee(order, baristaName)
        send(
            coffee
        )
    }
}

@OptIn(ExperimentalTime::class)
suspend fun prepareCoffee(order: Order, baristaName: String): CoffeeResult {
    val timeTaken = measureTimeMillis {
        delay(Random.nextLong(5000))
    }
    return CoffeeResult(
        orderId = order.id,
        coffee = order.coffeeType,
        customer = order.customer,
        baristaName = baristaName,
        preparedFor = timeTaken.toString()
    )
}

fun <T> CoroutineScope.merge(
    vararg channels: ReceiveChannel<T>
): ReceiveChannel<T> = produce {
    for (channel in channels) {
        launch {
            for (elem in channel) {
                send(elem)
            }
        }
    }
}

data class Order(
    val id: String,
    val coffeeType: String,
    val customer: String,
)

data class CoffeeResult(
    val orderId: String,
    val coffee: String,
    val customer: String,
    val baristaName: String,
    val preparedFor: String
)