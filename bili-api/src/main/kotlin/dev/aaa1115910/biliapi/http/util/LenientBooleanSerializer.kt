package dev.aaa1115910.biliapi.http.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.int
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonPrimitive

object LenientBooleanSerializer : KSerializer<Boolean> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LenientBoolean", PrimitiveKind.BOOLEAN)

    override fun serialize(encoder: Encoder, value: Boolean) {
        encoder.encodeBoolean(value)
    }

    override fun deserialize(decoder: Decoder): Boolean {
        if (decoder is JsonDecoder) {
            val element = decoder.decodeJsonElement()
            val primitive = element.jsonPrimitive
            if (primitive.isString) {
                return primitive.content.toBoolean() || primitive.content == "1"
            }
            if (primitive.booleanOrNull != null) {
                return primitive.boolean
            }
            if (primitive.intOrNull != null) {
                return primitive.int == 1
            }
            return false
        }
        return try {
            decoder.decodeBoolean()
        } catch (e: Exception) {
            false
        }
    }
}
