package io.github.justincodinguk.c_hat_server_v2.core.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.SimpleDateFormat

class DateFormatSerializer : KSerializer<SimpleDateFormat> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("SimpleDateFormat", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): SimpleDateFormat {
        return SimpleDateFormat(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: SimpleDateFormat) {
        encoder.encodeString(value.toPattern())
    }

}