package io.github.justincodinguk.c_hat_server_v2.model

import org.jetbrains.exposed.sql.Table

data class ChatUser(
    val clientId: String,
    val mailId: String,
    val username: String,
    val password: String
)

object ChatUsers: Table() {
    val id = varchar("client_id", 5)
    val mailId = varchar("mail_id", 100)
    val username = varchar("username", 100)
    val password = varchar("password", 100)

    override val primaryKey = PrimaryKey(id)
}

