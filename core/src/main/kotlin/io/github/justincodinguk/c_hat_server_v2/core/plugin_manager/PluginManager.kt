package io.github.justincodinguk.c_hat_server_v2.core.plugin_manager

import io.github.justincodinguk.c_hat_server_v2.eventbus.Event
import io.github.justincodinguk.c_hat_server_v2.eventbus.EventBus
import io.github.justincodinguk.c_hat_server_v2.eventbus.OnEventCallback
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URLClassLoader
import java.nio.file.Files
import java.nio.file.Paths
import java.util.jar.JarFile
import kotlin.io.path.name
import kotlin.io.path.pathString


class PluginManager(private val eventBus: EventBus) {

    fun loadPluginsFromDirectory(directory: String) {
        for(file in Files.walk(Paths.get(directory))) {
            if(file.name.endsWith(".jar")) {
                try {
                    val jarFile = JarFile(file.pathString)
                    val pluginConfStream = jarFile.getInputStream(jarFile.getEntry("plugins.json"))
                    val pluginConf = Json.decodeFromString<PluginConfig>(BufferedReader(InputStreamReader(pluginConfStream)).readText())

                    val classLoader = URLClassLoader(arrayOf(file.toUri().toURL()))
                    val runnerClass = classLoader.loadClass(pluginConf.pluginClassFullyQualifiedName)

                    val runnerClassInstance = runnerClass.getConstructor().newInstance()
                    val allowedMethods = buildMap {
                        Event.values().forEach { put(it.toFunctionName(), it) }
                    }

                    runnerClass.methods.forEach {
                        if(it.name in allowedMethods.keys) {
                            val event = allowedMethods[it.name]!!
                            eventBus.registerTask(event, OnEventCallback(pluginConf.pluginId, it, runnerClassInstance))
                        }
                    }

                } catch(e: Exception) {
                    //throw IllegalArgumentException("Invalid plugin ${file.name}")
                    throw e
                }
            }
        }
    }

    fun dynamicUnload(pluginId: String) = eventBus.unregisterCallbacksFor(pluginId)

    fun loadFromSource(pluginId: String) {
        try {
            val className = "io.github.justincodinguk.c_hat_server_v2.external_plugins.PluginRunner"
            val pluginClass = ClassLoader.getSystemClassLoader().loadClass(className)
            val pluginClassInstance = pluginClass.getConstructor().newInstance()
            for (method in pluginClass.methods) {
                if(method.name == "${pluginId}Runner") {
                    method.invoke(pluginClassInstance, eventBus)
                    return
                }
            }
            throw IllegalArgumentException("The plugin with id $pluginId was not found in the source")
        } catch (_: ClassNotFoundException) {
            throw IllegalArgumentException("The source has no plugins.")
        }
    }
}