package io.github.justincodinguk.c_hat_server_v2.core.responses

import io.github.justincodinguk.c_hat_server_v2.core.responses.event.Event

sealed interface Response {
    val event: Event
}
