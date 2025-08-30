package com.kotlinserialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.time.OffsetDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun main() {
    val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    val env = json.decodeFromString<VcJwtTokenInfo>(VC_JWT_JSON)
    val vc = env.vc

    println("### SUBJECT CLAIMS ###")
    for (subject in vc.credentialSubject) {
        subject.forEach { (k, v) -> println("${k.normalize()}: $v") }
    }

    println()
    println("### VC CLAIMS ###")
    val validFrom = vc.validFrom
    println("validFrom: ${validFrom.toLocalDate()}")
    val validUntil = vc.validUntil
    println("validUntil: ${validUntil.toLocalDate()}")
    val vcType = vc.vcType
    println("vcType: $vcType")
}

// ========= VC JWT Data =========

@Serializable
data class VcJwtTokenInfo(
    val iss: String,
    val sub: String,
    val vc: VerifiableCredentialInfo,
)

// ========= VC with arbitrary subject's claims =========

@Serializable
data class VerifiableCredentialInfo(
    @Serializable(with = OneOrManyJsonObjectSerializer::class)
    @SerialName("credentialSubject")
    val credentialSubject: List<JsonObject>,

    val id: String,
    val issuer: String? = null,

    @Serializable(with = OneOrManyStringSerializer::class)
    @SerialName("@context")
    val context: List<String> = emptyList(),

    val type: List<String> = emptyList(),

    @Serializable(with = ZonedDateTimeSerializer::class)
    val validFrom: ZonedDateTime,

    @Serializable(with = ZonedDateTimeSerializer::class)
    val validUntil: ZonedDateTime,

    val credentialStatus: JsonObject? = null,
) {
    val vcType: String? get() = type.filterNot { it == "VerifiableCredential" }.firstOrNull()
}

/** Accepts object OR array-of-objects and returns List<JsonObject>. */
object OneOrManyJsonObjectSerializer : KSerializer<List<JsonObject>> {
    private val listSer = ListSerializer(JsonObject.serializer())
    override val descriptor = listSer.descriptor
    override fun deserialize(decoder: Decoder): List<JsonObject> {
        val el = (decoder as? JsonDecoder)?.decodeJsonElement()
            ?: error("OneOrManyJsonObjectSerializer expects Json input")
        return when (el) {
            is JsonObject -> listOf(el)
            is JsonArray -> el.map { it.jsonObject }
            else -> emptyList()
        }
    }

    override fun serialize(encoder: Encoder, value: List<JsonObject>) {
        val jsonEncoder = encoder as JsonEncoder
        if (value.size == 1) {
            jsonEncoder.encodeJsonElement(value.first())
        } else {
            jsonEncoder.encodeJsonElement(JsonArray(value))
        }
    }
}

/** Accepts string OR array-of-strings and returns List<String>. */
object OneOrManyStringSerializer : KSerializer<List<String>> {
    private val listSer = ListSerializer(String.serializer())
    override val descriptor = listSer.descriptor
    override fun deserialize(decoder: Decoder): List<String> {
        val el = (decoder as JsonDecoder).decodeJsonElement()
        return when (el) {
            is JsonPrimitive -> listOf(el.content)
            is JsonArray -> el.map { it.jsonPrimitive.content }
            else -> emptyList()
        }
    }

    override fun serialize(encoder: Encoder, value: List<String>) {
        val jsonEncoder = encoder as JsonEncoder
        if (value.size == 1) {
            jsonEncoder.encodeJsonElement(JsonPrimitive(value.first()))
        } else {
            jsonEncoder.encodeJsonElement(JsonArray(value.map { JsonPrimitive(it) }))
        }
    }
}

fun JsonObject.stringList(name: String): List<String>? =
    when (val el = this[name]) {
        is JsonArray -> el.mapNotNull { it.jsonPrimitive.contentOrNull }
        is JsonPrimitive -> listOfNotNull(el.contentOrNull)
        else -> null
    }

object ZonedDateTimeSerializer : KSerializer<ZonedDateTime> {
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): ZonedDateTime {
        val dateString = decoder.decodeString()
        val offsetDateTime = OffsetDateTime.parse(dateString, formatter)
        return offsetDateTime.toZonedDateTime()
    }

    override fun serialize(encoder: Encoder, value: ZonedDateTime) {
        val formatted = formatter.format(value)
        encoder.encodeString(formatted)
    }
}

fun String.normalize() = this.lowercase()
    .replaceFirstChar(Char::titlecase)
    .replace("_", " ")

const val VC_JWT_JSON = "{\n" +
        "  \"iss\": \"did:key:z4MXj1wBzi9jUstyP3oYVDZ9yV2iTukTsuq7QHsZKQjTrPmHhofxVC7DA9xL9hTFyRmpGbz1svPR656E53bHRHDYnKWNEZN2mfE6911wDfqfWhyDx5GzeCTFiL3gbuZkwu223Led6XxCZ18rbW93KQsMoi8QeQk3uBt4EuwhYtbPv2zSTwCf6tUmMi7URUW1KEiiWc6xpzjrFHp3PkorLfNc7LWYpkbARx29Nn5qppLKW2mHZUiyu1Rr2ac3emfAs7F8JhS1XDH3SXe5GeiJz4DFi9se3fWzuFxBaYHp2UB4DGZUjckR3UxezagQDx33rzHBCmyGuP3YpysM48U4bfV9KomCtzGKcwdiung6mjYpzt3iwFjDr\",\n" +
        "  \"sub\": \"did:key:zWmu9RCrS3hBhGSyhKfNSywuTWFCMjMxJeKhMPt6jui8aWH9XjHzTj3nK9Rx6dKyWFeTrJT7xuk2n37mfUwxWW1pS2nd3Ljza3rWs4oJjeV3EnnXn6UtrSNdG17Hg4Nu\",\n" +
        "  \"vc\": {\n" +
        "    \"credentialSubject\": {\n" +
        "      \"issuance_date\": \"2023-10-10\",\n" +
        "      \"document_number\": \"111111111\",\n" +
        "      \"nationality\": [\n" +
        "        \"BG\"\n" +
        "      ],\n" +
        "      \"age_over_18\": true,\n" +
        "      \"issuing_country\": \"BG\",\n" +
        "      \"issuing_authority\": \"MOL BGR\",\n" +
        "      \"sex\": 1,\n" +
        "      \"birth_date\": \"1999-02-20\",\n" +
        "      \"expiry_date\": \"2038-10-10\",\n" +
        "      \"birth_place\": \"DAGESTAN\",\n" +
        "      \"given_name\": \"TEST UBBLE\",\n" +
        "      \"family_name\": \"BOEV\",\n" +
        "      \"id\": \"did:key:zWmu9RCrS3hBhGSyhKfNSywuTWFCMjMxJeKhMPt6jui8aWH9XjHzTj3nK9Rx6dKyWFeTrJT7xuk2n37mfUwxWW1pS2nd3Ljza3rWs4oJjeV3EnnXn6UtrSNdG17Hg4Nu\"\n" +
        "    },\n" +
        "    \"validUntil\": \"2026-04-27T22:59:51.156410039Z\",\n" +
        "    \"id\": \"urn:uuid:48816225-3096-440f-8f0a-ac3ae016f7e9\",\n" +
        "    \"validFrom\": \"2025-04-27T22:59:50.765107040Z\",\n" +
        "    \"type\": [\n" +
        "      \"VerifiableCredential\",\n" +
        "      \"IdentityCredential\"\n" +
        "    ],\n" +
        "    \"@context\": [\n" +
        "      \"https://www.w3.org/ns/credentials/v2\"\n" +
        "    ],\n" +
        "    \"issuer\": \"did:key:z4MXj1wBzi9jUstyP3oYVDZ9yV2iTukTsuq7QHsZKQjTrPmHhofxVC7DA9xL9hTFyRmpGbz1svPR656E53bHRHDYnKWNEZN2mfE6911wDfqfWhyDx5GzeCTFiL3gbuZkwu223Led6XxCZ18rbW93KQsMoi8QeQk3uBt4EuwhYtbPv2zSTwCf6tUmMi7URUW1KEiiWc6xpzjrFHp3PkorLfNc7LWYpkbARx29Nn5qppLKW2mHZUiyu1Rr2ac3emfAs7F8JhS1XDH3SXe5GeiJz4DFi9se3fWzuFxBaYHp2UB4DGZUjckR3UxezagQDx33rzHBCmyGuP3YpysM48U4bfV9KomCtzGKcwdiung6mjYpzt3iwFjDr\",\n" +
        "    \"credentialStatus\": {\n" +
        "      \"id\": \"https://vc-issuer-dev.tinqin-dev.com/revocation/list/1#128\",\n" +
        "      \"type\": \"BitstringStatusListEntry\",\n" +
        "      \"statusPurpose\": \"revocation\",\n" +
        "      \"statusListIndex\": \"128\",\n" +
        "      \"statusListCredential\": \"https://vc-issuer-dev.tinqin-dev.com/revocation/list/1\"\n" +
        "    }\n" +
        "  }\n" +
        "}"
