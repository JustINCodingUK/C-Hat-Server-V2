package io.github.justincodinguk.c_hat_server_v2.model

data class ChatUser(
    val clientId: String,
    val mailId: String,
    val username: String,
    val password: String,
    val isOnline: Boolean
)