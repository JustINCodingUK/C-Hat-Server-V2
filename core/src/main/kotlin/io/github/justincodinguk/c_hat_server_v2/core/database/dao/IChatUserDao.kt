package io.github.justincodinguk.c_hat_server_v2.core.database.dao

import io.github.justincodinguk.c_hat_server_v2.model.ChatUser

interface IChatUserDao {

    suspend fun userExists(mailId: String): Boolean
    suspend fun getUserByMailId(mailId: String): ChatUser
    suspend fun insert(user: ChatUser)
    suspend fun generateClientId(): String
    suspend fun getAllUsers(): List<ChatUser>
    suspend fun getUserByClientId(clientId: String): ChatUser
    suspend fun setUserOnlineStatus(clientId: String, isOnline: Boolean)

}
