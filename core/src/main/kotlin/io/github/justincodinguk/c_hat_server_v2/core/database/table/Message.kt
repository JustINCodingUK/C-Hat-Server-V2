package io.github.justincodinguk.c_hat_server_v2.core.database.table

import org.jetbrains.exposed.sql.Table

object UnreadMessages: Table() {
    private val id = integer("primary_key").autoIncrement()
    val message = varchar("message", 5000)
    val time = varchar("time", 50)
    val author = varchar("author", 100)
    val recipientClientId = varchar("rcid", 5)

    override val primaryKey = PrimaryKey(id)
}