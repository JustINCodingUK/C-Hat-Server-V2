package io.github.justincodinguk.c_hat_server_v2.core.database.dao

import io.github.justincodinguk.c_hat_server_v2.model.ChatUser
import io.github.justincodinguk.c_hat_server_v2.core.database.DatabaseFactory.dbQuery
import io.github.justincodinguk.c_hat_server_v2.core.database.table.ChatUsers
import org.jetbrains.exposed.sql.*

class ChatUserDao : IChatUserDao {

    private fun ResultRow.toChatUser(): ChatUser {
        return ChatUser(
            clientId = this[ChatUsers.id],
            mailId = this[ChatUsers.mailId],
            username = this[ChatUsers.username],
            password = this[ChatUsers.password],
            isOnline = this[ChatUsers.isOnline]
        )
    }

    override suspend fun userExists(mailId: String): Boolean = dbQuery {
        ChatUsers.selectAll().any {
            it.toChatUser().mailId == mailId
        }
    }

    override suspend fun getUserByMailId(mailId: String): ChatUser = dbQuery {
        ChatUsers.select {
            ChatUsers.mailId eq mailId
        }.map {
            it.toChatUser()
        }.single()
    }

    override suspend fun insert(user: ChatUser): Unit = dbQuery {
        ChatUsers.insert {
            it[id] = user.clientId
            it[mailId] = user.mailId
            it[password] = user.password
            it[username] = user.username
            it[isOnline] = user.isOnline
        }
    }

    override suspend fun generateClientId(): String = dbQuery {
        var clientId: String
        val users = ChatUsers.selectAll()
        do {
            clientId = getRandomString(5)
        } while(users.any { it.toChatUser().clientId == clientId })
        clientId
    }

    override suspend fun getUserByClientId(clientId: String): ChatUser  = dbQuery {
        ChatUsers.select {
            ChatUsers.id eq clientId
        }.map {
            it.toChatUser()
        }.single()
    }

    override suspend fun getAllUsers(): List<ChatUser>  = dbQuery {
        ChatUsers.selectAll().map {
            it.toChatUser()
        }
    }

    override suspend fun setUserOnlineStatus(clientId: String, isOnline: Boolean): Unit = dbQuery {
        ChatUsers.update(where = {
            ChatUsers.id eq clientId
        }) {
            it[this.isOnline] = isOnline
        }
    }

    private fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
}
