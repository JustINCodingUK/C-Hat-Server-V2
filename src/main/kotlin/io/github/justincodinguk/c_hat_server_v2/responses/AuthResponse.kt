package io.github.justincodinguk.c_hat_server_v2.responses

import io.github.justincodinguk.c_hat_server_v2.responses.event.Event
import io.github.justincodinguk.c_hat_server_v2.responses.status.Status
import kotlinx.serialization.*
import kotlinx.serialization.json.JsonNames

@Serializable
sealed interface AuthResponse : Response

@Serializable
data class AuthRequestReceivedResponse @OptIn(ExperimentalSerializationApi::class) constructor(
    override val event: Event = Event.AUTH,
    @JsonNames("mail_id") val mailId: String,
    val password: String
) : AuthResponse

@Serializable
data class AuthErrorResponse @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault
    override val event: Event = Event.AUTH,
    val status: Status = Status.ERROR,
    val message: String
) : AuthResponse

@Serializable
data class AuthSuccessResponse @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault
    override val event: Event = Event.AUTH,
    @EncodeDefault
    val status: Status = Status.DONE,
    @SerialName("client_id") val clientId: String,
    val username: String
) : AuthResponse
