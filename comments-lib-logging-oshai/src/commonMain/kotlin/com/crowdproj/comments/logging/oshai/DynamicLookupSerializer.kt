import kotlinx.serialization.*
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonUnquotedLiteral

@OptIn(ExperimentalSerializationApi::class)
object DynamicLookupSerializer : KSerializer<Any> {
    override val descriptor: SerialDescriptor =
        ContextualSerializer(Any::class, null, emptyArray()).descriptor

    @Suppress("UNCHECKED_CAST")
    @OptIn(InternalSerializationApi::class)
    override fun serialize(encoder: Encoder, value: Any) {
        val actualSerializer = encoder.serializersModule.getContextual(value::class)
            ?: value::class.serializer()
        if (value is String) {
            val jsonUnquotedLiteral = JsonUnquotedLiteral(value.takeIf { it != "null" })
            encoder.encodeSerializableValue(JsonPrimitive.serializer(), jsonUnquotedLiteral)
        } else {
            encoder.encodeSerializableValue(actualSerializer as KSerializer<Any>, value)
        }
    }

    override fun deserialize(decoder: Decoder): Any {
        error("Unsupported")
    }
}