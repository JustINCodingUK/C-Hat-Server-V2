package io.github.justincodinguk.c_hat_server_v2.core

import io.github.justincodinguk.c_hat_server_v2.core.database.DatabaseFactory
import io.github.justincodinguk.c_hat_server_v2.eventbus.EventBus
import io.github.justincodinguk.c_hat_server_v2.core.plugins.configureSockets
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {

    val eventBus = EventBus()

    try {

        val pluginRunnerClass = ClassLoader.getSystemClassLoader().loadClass("io.github.justincodinguk.c_hat_server_v2.external_plugins.PluginRunner")
        val pluginRunners = pluginRunnerClass.methods

        val pluginRunner = pluginRunnerClass.getConstructor().newInstance()

        for(function in pluginRunners) {
            if(function.name.contains("Runner")) {
                log.info("PluginRunner: Calling plugin ${function.name}")
                function.invoke(pluginRunner, eventBus)
            }
        }
    } catch (_: ClassNotFoundException){
        log.info("PluginLoader: No plugins found")
    }

    environment.monitor.subscribe(ApplicationStarted) {
        eventBus.start()
    }

    environment.monitor.subscribe(ApplicationStopped) {
        eventBus.stop()
    }

    DatabaseFactory.init()
    configureSockets(eventBus)
}