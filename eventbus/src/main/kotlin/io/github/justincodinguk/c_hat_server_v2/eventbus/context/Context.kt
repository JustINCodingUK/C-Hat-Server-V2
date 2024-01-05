package io.github.justincodinguk.c_hat_server_v2.eventbus.context

import io.github.justincodinguk.c_hat_server_v2.model.Message

sealed interface Context

class MessageContext(
    val message: Message
) : Context

class ClientIdContext(
    val clientId: String
) : Context

object NoParamContext : Context