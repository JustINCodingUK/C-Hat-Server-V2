package io.github.justincodinguk.c_hat_server_v2.responses

import io.github.justincodinguk.c_hat_server_v2.responses.event.Event

sealed interface Response {
    val event: Event
}
