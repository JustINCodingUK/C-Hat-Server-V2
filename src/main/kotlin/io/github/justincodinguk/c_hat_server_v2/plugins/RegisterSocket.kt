package io.github.justincodinguk.c_hat_server_v2.plugins

import io.github.justincodinguk.c_hat_server_v2.database.dao.IChatUserDao
import io.github.justincodinguk.c_hat_server_v2.email
import io.github.justincodinguk.c_hat_server_v2.model.ChatUser
import io.github.justincodinguk.c_hat_server_v2.responses.*
import io.github.justincodinguk.c_hat_server_v2.responses.event.Event
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.random.Random

fun Routing.registerWebSocket(endpoint: String, dao: IChatUserDao) = webSocket(endpoint) {
    val verificationCode = Random.nextInt(100000,999999)
    var newUser: ChatUser? = null
    for(frame in incoming) {
        if(frame is Frame.Text) {
            val deserializedResponse = try {
                Json.decodeFromString<RegisterRequestReceivedResponse>(frame.readText())
            } catch(e: Exception) {
                Json.decodeFromString<VerificationCodeReceivedResponse>(frame.readText())
            }

            if(deserializedResponse is RegisterRequestReceivedResponse) {
                newUser = ChatUser(
                    clientId = dao.generateClientId(),
                    mailId = deserializedResponse.mailId,
                    username = deserializedResponse.username,
                    password = deserializedResponse.password
                )
            }
            if(deserializedResponse.event == Event.REGISTER) {
                if(dao.userExists((deserializedResponse as RegisterRequestReceivedResponse).mailId)) {
                    val response = RegisterFailedResponse(message = "User with mail id already exists")
                    send(Json.encodeToString(response))
                    close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT, response.message))
                } else {
                    val emailContent = "Hey ${deserializedResponse.username}! Thanks for joining this community.\n Your verification code is $verificationCode"
                    email(emailContent, deserializedResponse.mailId).send()
                    send(Json.encodeToString(RegisterAwaitsResponse()))
                }
            } else if(deserializedResponse.event == Event.CONFIRMATION) {
                if(verificationCode == (deserializedResponse as VerificationCodeReceivedResponse).code) {
                    if(newUser != null) {
                        dao.insert(newUser)
                        send(Json.encodeToString(RegisterSuccessResponse()))
                        close(CloseReason(CloseReason.Codes.NORMAL, "Registration Successful"))
                    }
                } else {
                    val response = RegisterFailedResponse(message = "Incorrect verification code")
                    send(Json.encodeToString(response))
                    close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT, response.message))
                }
            }
        }
    }
}