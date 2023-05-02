package io.github.justincodinguk.c_hat_server_v2.responses.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Event {
    @SerialName("message")
    MESSAGE,

    @SerialName("register")
    REGISTER,

    @SerialName("confirmation")
    CONFIRMATION,

    @SerialName("auth")
    AUTH;

    override fun toString(): String {
        return super.toString().removePrefix("Event.").lowercase()
    }
}