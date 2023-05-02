package io.github.justincodinguk.c_hat_server_v2

import io.github.justincodinguk.c_hat_server_v2.database.DatabaseFactory
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.github.justincodinguk.c_hat_server_v2.plugins.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    DatabaseFactory.init()
    configureSockets()
}

