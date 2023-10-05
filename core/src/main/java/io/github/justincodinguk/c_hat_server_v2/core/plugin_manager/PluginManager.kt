package io.github.justincodinguk.c_hat_server_v2.core.plugin_manager

import io.github.justincodinguk.c_hat_server_v2.eventbus.EventBus
import java.net.URLClassLoader
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.name

class PluginManager(private val eventBus: EventBus) {

    fun loadPluginsFromDirectory(directory: String) {
        for(file in Files.walk(Paths.get(directory))) {
            if(file.name.endsWith(".jar")) {
                val pluginId = file.name.split("-v")[0]
                val classLoader = URLClassLoader(arrayOf(file.toUri().toURL()))
                val runnerClass = classLoader.loadClass("io.github.justincodinguk.c_hat_server_v2.external_plugins.PluginRunner")
                val runnerClassInstance = runnerClass.getConstructor().newInstance()
                for(method in runnerClass.methods) {
                    if (method.name == "${pluginId}Runner") {
                        method.invoke(runnerClassInstance, eventBus)
                    }
                }
            }
        }
    }

    fun loadFromSource(pluginId: String) {
        try {
            val className = "io.github.justincodinguk.c_hat_server_v2.external_plugins.PluginRunner"
            val pluginClass = ClassLoader.getSystemClassLoader().loadClass(className)
            val pluginClassInstance = pluginClass.getConstructor().newInstance()
            for (method in pluginClass.methods) {
                if(method.name == "${pluginId}Runner") {
                    method.invoke(pluginClassInstance, eventBus)
                    break
                }
            }
        } catch (_: ClassNotFoundException) {
            throw IllegalArgumentException("The requested plugin was not found in the source. It might be an external plugin?")
        }
    }

}