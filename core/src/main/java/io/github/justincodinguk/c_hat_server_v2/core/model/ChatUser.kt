package io.github.justincodinguk.c_hat_server_v2.core.model

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.booleanParam

data class ChatUser(
    val clientId: String,
    val mailId: String,
    val username: String,
    val password: String,
    val isOnline: Boolean
)

object ChatUsers: Table() {
    val id = varchar("client_id", 5)
    val mailId = varchar("mail_id", 100)
    val username = varchar("username", 100)
    val password = varchar("password", 100)
    val isOnline = bool("is_online")

    override val primaryKey = PrimaryKey(id)
}

