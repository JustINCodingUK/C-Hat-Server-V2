package io.github.justincodinguk.c_hat_server_v2.responses

import io.github.justincodinguk.c_hat_server_v2.responses.event.Event
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

sealed interface MessageResponse : Response

@Serializable
data class MessageReceivedResponse @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault
    override val event: Event = Event.MESSAGE,
    val message: String,
    val time: String,
    @JsonNames("rcid")
    @SerialName("rcid")
    val recipientClientId: String,
    val author: String
) : MessageResponse
