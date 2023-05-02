package io.github.justincodinguk.c_hat_server_v2.plugins

import io.github.justincodinguk.c_hat_server_v2.database.dao.IChatUserDao
import io.github.justincodinguk.c_hat_server_v2.responses.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val connections = mutableMapOf<String, DefaultWebSocketServerSession>()

fun Routing.chatWebSocket(endpoint: String, dao: IChatUserDao) = webSocket(endpoint) {
    var isAuthorised = false
    for(frame in incoming) {
        if(frame is Frame.Text) {

            if(isAuthorised) {
                val message = Json.decodeFromString<MessageReceivedResponse>(frame.readText())
                connections[message.recipientClientId]?.send(Json.encodeToString(message))
            } else {
                val deserializedResponse = Json.decodeFromString<AuthRequestReceivedResponse>(frame.readText())
                val authorisedResponse = authorise(deserializedResponse, dao)

                send(Json.encodeToString(authorisedResponse))
                if(authorisedResponse is AuthErrorResponse) {
                    close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT, authorisedResponse.message))
                } else {
                    isAuthorised = true
                    connections[(authorisedResponse as AuthSuccessResponse).clientId] = this
                }
            }
        }
    }

}

private suspend fun authorise(authResponse: AuthRequestReceivedResponse, dao: IChatUserDao): AuthResponse {
    if(!dao.userExists(authResponse.mailId)) {
        return AuthErrorResponse(message = "User doesn't exist on the server")
    }

    return if(dao.getUserByMailId(authResponse.mailId).password == authResponse.password) {
        val user = dao.getUserByMailId(authResponse.mailId)
        AuthSuccessResponse(
            clientId = user.clientId,
            username = user.username
        )
    } else {
        AuthErrorResponse(message = "Incorrect password")
    }

}