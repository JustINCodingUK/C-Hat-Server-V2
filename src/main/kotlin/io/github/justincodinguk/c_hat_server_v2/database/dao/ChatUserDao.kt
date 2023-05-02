package io.github.justincodinguk.c_hat_server_v2.database.dao

import io.github.justincodinguk.c_hat_server_v2.model.ChatUser
import io.github.justincodinguk.c_hat_server_v2.database.DatabaseFactory.dbQuery
import io.github.justincodinguk.c_hat_server_v2.model.ChatUsers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class ChatUserDao : IChatUserDao {

    private fun ResultRow.toChatUser(): ChatUser {
        return ChatUser(
            clientId = this[ChatUsers.id],
            mailId = this[ChatUsers.mailId],
            username = this[ChatUsers.username],
            password = this[ChatUsers.password]
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
        }
    }

    override suspend fun generateClientId(): String = dbQuery {
        var clientId = getRandomString(5)
        while(ChatUsers.selectAll().any { it.toChatUser().clientId == clientId }) {
            clientId = getRandomString(5)
        }
        clientId
    }

    override suspend fun getAllUsers(): List<ChatUser>  = dbQuery {
        ChatUsers.selectAll().map {
            it.toChatUser()
        }
    }

    private fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
}
