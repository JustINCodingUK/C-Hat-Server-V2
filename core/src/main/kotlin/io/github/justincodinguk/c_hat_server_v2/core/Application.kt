package io.github.justincodinguk.c_hat_server_v2.core

import io.github.justincodinguk.c_hat_server_v2.core.database.DatabaseFactory
import io.github.justincodinguk.c_hat_server_v2.core.plugin_manager.PluginManager
import io.github.justincodinguk.c_hat_server_v2.eventbus.EventBus
import io.github.justincodinguk.c_hat_server_v2.core.sockets.configureSockets
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {

    val config = GlobalRepository.config

    val defaultHostOnDebug = config.defaultHostOnDebug && config.isDebugModeEnabled

    embeddedServer(
        Netty,
        port = if(defaultHostOnDebug) 8080 else config.port,
        host = if(defaultHostOnDebug) "0.0.0.0" else config.host,
        module = Application::module
    ).start(wait = true)

}

fun Application.module() {

    val eventBus = EventBus()
    val pluginManager = PluginManager(eventBus)

    pluginManager.loadPluginsFromDirectory("plugins")

    environment.monitor.subscribe(ApplicationStarted) {
        eventBus.start()
    }

    environment.monitor.subscribe(ApplicationStopped) {
        eventBus.stop()
    }

    DatabaseFactory.init()
    configureSockets(eventBus)
}