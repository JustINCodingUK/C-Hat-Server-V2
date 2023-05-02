package io.github.justincodinguk.c_hat_server_v2.plugins

import io.github.justincodinguk.c_hat_server_v2.database.dao.ChatUserDao
import io.ktor.server.websocket.*
import java.time.Duration
import io.ktor.server.application.*
import io.ktor.server.routing.*


fun Application.configureSockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(100000)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
    val dao = ChatUserDao()

    routing {
        chatWebSocket("/chat", dao)
        registerWebSocket("/register", dao)
    }
}
