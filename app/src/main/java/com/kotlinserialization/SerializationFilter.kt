package com.kotlinserialization

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable
class Project2(val name: String, val language: String)

object ProjectSerializer : JsonTransformingSerializer<Project2>(Project2.serializer()) {
    override fun transformSerialize(element: JsonElement): JsonElement =
    // Filter out top-level key value pair with the key "language" and the value "Kotlin"
        // Same can be achieved by @EncodeDefault(EncodeDefault.Mode.NEVER), when default value is given to the property.
        JsonObject(element.jsonObject.filterNot { (k, v) ->
            k == "language" && v.jsonPrimitive.content == "Kotlin"
        })
}

fun main() {
    val data = Project2("kotlinx.serialization", "Kotlin")
    println(Json.encodeToString(data)) // using plugin-generated serializer
    println(Json.encodeToString(ProjectSerializer, data)) // using custom serializer
}