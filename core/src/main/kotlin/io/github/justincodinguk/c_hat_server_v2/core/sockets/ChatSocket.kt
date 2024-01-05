package io.github.justincodinguk.c_hat_server_v2.core.sockets

import io.github.justincodinguk.c_hat_server_v2.core.responses.*
import io.github.justincodinguk.c_hat_server_v2.core.database.dao.IChatUserDao
import io.github.justincodinguk.c_hat_server_v2.core.database.dao.IMessageDao
import io.github.justincodinguk.c_hat_server_v2.model.Message
import io.github.justincodinguk.c_hat_server_v2.eventbus.Event
import io.github.justincodinguk.c_hat_server_v2.eventbus.EventBus
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val connections = mutableMapOf<String, DefaultWebSocketServerSession>()

fun Routing.chatWebSocket(
    endpoint: String,
    userDao: IChatUserDao,
    messageDao: IMessageDao,
    eventBus: EventBus
) = webSocket(endpoint) {

    var clientId = String()
    eventBus.fireEvent(Event.ON_CLIENT_CONNECT)
    var isAuthorised = false
    for(frame in incoming) {
        if(frame is Frame.Text) {
            if(isAuthorised) {

                val message = Json.decodeFromString<MessageReceivedResponse>(frame.readText())
                val messageObj = Message(
                    message = message.message,
                    time = message.time,
                    author = message.author,
                    recipientClientId = message.recipientClientId
                )
                eventBus.fireEvent(Event.ON_MESSAGE_RECEIVED, messageObj)
                if(userDao.getUserByClientId(message.recipientClientId).isOnline) {
                    connections[message.recipientClientId]?.send(Json.encodeToString(message))
                } else {
                    messageDao.insert(messageObj)
                }

            } else {
                val deserializedResponse = Json.decodeFromString<AuthRequestReceivedResponse>(frame.readText())
                val authorisedResponse = authorise(deserializedResponse, userDao, eventBus)

                send(Json.encodeToString(authorisedResponse))
                if(authorisedResponse is AuthErrorResponse) {
                    close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT, authorisedResponse.message))
                } else {
                    isAuthorised = true
                    connections[(authorisedResponse as AuthSuccessResponse).clientId] = this
                    clientId = authorisedResponse.clientId
                    userDao.setUserOnlineStatus(authorisedResponse.clientId, true)
                    if(messageDao.getUnreadMessagesForClientId(authorisedResponse.clientId).isNotEmpty()) {
                        for(message in messageDao.getUnreadMessagesForClientId(authorisedResponse.clientId)) {
                            val messageResponse = MessageReceivedResponse(
                                message = message.message,
                                time = message.time,
                                author = message.author,
                                recipientClientId = message.recipientClientId
                            )
                            send(Json.encodeToString(messageResponse))
                        }
                        messageDao.deleteAllFor(authorisedResponse.clientId)
                    }
                }
            }
        }
    }
    userDao.setUserOnlineStatus(clientId, false)
    connections.remove(clientId)
    eventBus.fireEvent(Event.ON_USER_DISCONNECT, clientId)
}

private suspend fun authorise(authResponse: AuthRequestReceivedResponse, dao: IChatUserDao, eventBus: EventBus): AuthResponse {
    if(!dao.userExists(authResponse.mailId)) {
        return AuthErrorResponse(message = "User doesn't exist on the server")
    }
    val user = dao.getUserByMailId(authResponse.mailId)
    return if(user.password == authResponse.password) {
        eventBus.fireEvent(Event.ON_USER_LOGIN, user.clientId)
        AuthSuccessResponse(
            clientId = user.clientId,
            username = user.username
        )
    } else {
        AuthErrorResponse(message = "Incorrect password")
    }

}