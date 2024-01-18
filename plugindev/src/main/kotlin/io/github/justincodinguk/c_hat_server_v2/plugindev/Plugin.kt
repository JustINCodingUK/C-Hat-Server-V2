package io.github.justincodinguk.c_hat_server_v2.plugindev

import io.github.justincodinguk.c_hat_server_v2.model.Message

@Suppress("UNUSED")
abstract class Plugin {

    abstract val pluginId: String

    open suspend fun onStart() {}

    open suspend fun onStop() {}

    open suspend fun onClientConnect() {}

    open suspend fun onUserLogin(clientId: String) {}

    open suspend fun onMessageReceived(message: Message) {}

    open suspend fun onUserDisconnect(clientId: String) {}

    open suspend fun onRegisterComplete(newClientId: String) {}

    open suspend fun onRegisterFailed(reason: String) {}

}