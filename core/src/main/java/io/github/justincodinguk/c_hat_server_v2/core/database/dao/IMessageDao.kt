package io.github.justincodinguk.c_hat_server_v2.core.database.dao

import io.github.justincodinguk.c_hat_server_v2.core.model.Message

interface IMessageDao {
    suspend fun insert(message: Message)
    suspend fun deleteAllFor(clientId: String)
    suspend fun getUnreadMessagesForClientId(clientId: String): List<Message>
}