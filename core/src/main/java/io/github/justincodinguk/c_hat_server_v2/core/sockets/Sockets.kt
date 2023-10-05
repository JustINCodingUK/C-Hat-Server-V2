package io.github.justincodinguk.c_hat_server_v2.core.sockets

import io.github.justincodinguk.c_hat_server_v2.core.database.dao.ChatUserDao
import io.github.justincodinguk.c_hat_server_v2.core.database.dao.MessageDao
import io.github.justincodinguk.c_hat_server_v2.eventbus.EventBus
import io.ktor.server.websocket.*
import java.time.Duration
import io.ktor.server.application.*
import io.ktor.server.routing.*


fun Application.configureSockets(eventBus: EventBus) {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(100000)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
    val userDao = ChatUserDao()
    val messageDao = MessageDao()

    routing {
        chatWebSocket("/chat", userDao, messageDao, eventBus)
        registerWebSocket("/register", userDao, eventBus)
    }
}
