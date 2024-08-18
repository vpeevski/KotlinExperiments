package com.kotlinserialization

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonTransformingSerializer

@Serializable
data class Project(
    val name: String,
    @Serializable(with = UserListSerializer::class)
    val users: List<User>
)

@Serializable
data class User(val name: String)


object UserListSerializer :
    JsonTransformingSerializer<List<User>>(ListSerializer(User.serializer())) {
    // If response is not an array, then it is a single object that should be wrapped into the array
    override fun transformDeserialize(element: JsonElement): JsonElement =
        if (element !is JsonArray) JsonArray(listOf(element)) else element

    // Unwrap list of users as single Json object
    override fun transformSerialize(element: JsonElement): JsonElement {
        require(element is JsonArray) // this serializer is used only with lists
        return element.singleOrNull() ?: element
    }
}

fun main() {
    // Wrap
    println(
        Json.decodeFromString<Project>(
            """
        {"name":"kotlinx.serialization","users":{"name":"kotlin"}}
    """
        )
    )
    println(
        Json.decodeFromString<Project>(
            """
        {"name":"kotlinx.serialization","users":[{"name":"kotlin"},{"name":"jetbrains"}]}
    """
        )
    )
    // Unwrap list of users as single Json object
    val data = Project("kotlinx.serialization", listOf(User("kotlin")))
    println(Json.encodeToString(data))
}
