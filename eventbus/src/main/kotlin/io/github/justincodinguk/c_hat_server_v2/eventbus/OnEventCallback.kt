package io.github.justincodinguk.c_hat_server_v2.eventbus

sealed interface OnEventCallback<in T> {
    val pluginId: String
    val callback: T.() -> Unit
}

