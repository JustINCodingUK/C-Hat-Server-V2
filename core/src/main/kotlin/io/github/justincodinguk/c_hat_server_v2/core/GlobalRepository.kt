package io.github.justincodinguk.c_hat_server_v2.core

import io.github.justincodinguk.c_hat_server_v2.core.config.Config
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.name
import kotlin.io.path.readText

object GlobalRepository {

    init { initializeConfig() }

    private lateinit var _configInstance: Config

    val config: Config
        get() {
            if(!::_configInstance.isInitialized) {
                throw IllegalStateException("Config hasn't been initialized yet")
            }
            return _configInstance
        }

    private fun initializeConfig() {
        for(file in Files.walk(Paths.get(""))) {
            if(file.name == "config.json") {
                val configRaw = file.readText()
                try {
                    _configInstance = Json.decodeFromString<Config>(configRaw)
                } catch (e: Exception) {
                    throw IllegalStateException("Invalid Config")
                }
            }
        }
        throw IllegalStateException("Config file not found")
    }
}