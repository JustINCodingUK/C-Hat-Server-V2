package io.github.justincodinguk.c_hat_server_v2.core.plugin_manager

import kotlinx.serialization.Serializable

@Serializable
data class PluginConfig(
    val pluginId: String,
    val isLib: Boolean,
    val version: String,
    val pluginClassFullyQualifiedName: String,
    val description: String
)
