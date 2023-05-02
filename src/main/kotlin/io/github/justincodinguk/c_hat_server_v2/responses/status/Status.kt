package io.github.justincodinguk.c_hat_server_v2.responses.status

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Status {

    @SerialName("done")
    DONE,

    @SerialName("awaits")
    AWAITS,

    @SerialName("error")
    ERROR;

    override fun toString(): String {
        return super.toString().removePrefix("Status.").lowercase()
    }

}