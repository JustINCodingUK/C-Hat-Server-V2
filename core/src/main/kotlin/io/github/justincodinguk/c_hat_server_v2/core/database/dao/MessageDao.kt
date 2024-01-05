package io.github.justincodinguk.c_hat_server_v2.core.database.dao

import io.github.justincodinguk.c_hat_server_v2.model.Message
import io.github.justincodinguk.c_hat_server_v2.core.database.table.UnreadMessages
import io.github.justincodinguk.c_hat_server_v2.core.database.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class MessageDao : IMessageDao {

    private fun ResultRow.toMessage(): Message {
        return Message(
            message = this[UnreadMessages.message],
            time = this[UnreadMessages.time],
            author = this[UnreadMessages.author],
            recipientClientId = this[UnreadMessages.recipientClientId]
        )
    }

    override suspend fun insert(message: Message): Unit = dbQuery {
        UnreadMessages.insert {
            it[this.message] = message.message
            it[time] = message.time
            it[author] = message.author
            it[recipientClientId] = message.recipientClientId
        }
    }

    override suspend fun deleteAllFor(clientId: String): Unit = dbQuery {
        UnreadMessages.deleteWhere {
            recipientClientId eq clientId
        }
    }

    override suspend fun getUnreadMessagesForClientId(clientId: String) : List<Message> = dbQuery {
        UnreadMessages.selectAll().map {
            it.toMessage()
        }.filter {
            it.recipientClientId == clientId
        }
    }

}