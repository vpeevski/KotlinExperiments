package com.kotlinserialization

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

fun main() {
    val element = Json.parseToJsonElement(
        """
        {
            "name": "kotlinx.serialization",
            "forks": [{"votes": 42}, {"votes": 9000}, {}]
        }
    """
    )
    val forksJsonObject = element.jsonObject["forks"]
    println("forksJsonObject: $forksJsonObject")
    println("forksJsonArray: ${forksJsonObject!!.jsonArray}")
    println("votes1JsonObject: ${forksJsonObject.jsonArray[0].jsonObject}")
    println("votes2JsonObject: ${forksJsonObject.jsonArray[1].jsonObject}")
    println("votes1Primitive: ${forksJsonObject.jsonArray[0].jsonObject["votes"]?.jsonPrimitive?.int}")
    println("votes2Primitive: ${forksJsonObject.jsonArray[1].jsonObject["votes"]?.jsonPrimitive?.int}")

    val sum = element
        .jsonObject["forks"]!!
        .jsonArray.sumOf { it.jsonObject["votes"]?.jsonPrimitive?.int ?: 0 }
    println(sum)
}