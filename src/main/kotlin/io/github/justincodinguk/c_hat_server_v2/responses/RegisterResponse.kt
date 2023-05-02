package io.github.justincodinguk.c_hat_server_v2.responses

import io.github.justincodinguk.c_hat_server_v2.responses.event.Event
import io.github.justincodinguk.c_hat_server_v2.responses.status.Status
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

sealed interface RegisterResponse : Response

@Serializable
data class RegisterRequestReceivedResponse @OptIn(ExperimentalSerializationApi::class) constructor(
    override val event: Event,
    val username: String,
    val password: String,
    @JsonNames("mail_id") val mailId: String
) : RegisterResponse

@Serializable
data class VerificationCodeReceivedResponse @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault
    override val event: Event = Event.CONFIRMATION,
    val code: Int
) : RegisterResponse

@Serializable
data class RegisterFailedResponse @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault
    override val event: Event = Event.REGISTER,
    val status: Status = Status.ERROR,
    val message: String
) : RegisterResponse

@Serializable
data class RegisterAwaitsResponse @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault
    override val event: Event = Event.REGISTER,
    @EncodeDefault
    val status: Status = Status.AWAITS
) : RegisterResponse

@Serializable
data class RegisterSuccessResponse @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault
    override val event: Event = Event.CONFIRMATION,
    @EncodeDefault
    val status: Status = Status.DONE
) : RegisterResponse