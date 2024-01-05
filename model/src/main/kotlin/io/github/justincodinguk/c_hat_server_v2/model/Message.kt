package io.github.justincodinguk.c_hat_server_v2.model

data class Message(
    val message: String,
    val time: String,
    val author: String,
    val recipientClientId: String
)
