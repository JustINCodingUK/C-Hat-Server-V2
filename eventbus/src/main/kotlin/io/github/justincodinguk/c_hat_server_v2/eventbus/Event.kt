package io.github.justincodinguk.c_hat_server_v2.eventbus

enum class Event {
    ON_CLIENT_CONNECT,
    ON_USER_LOGIN,
    ON_MESSAGE_RECEIVED,
    ON_START,
    ON_STOP,
    ON_USER_DISCONNECT,
    ON_REGISTER_COMPLETE,
    ON_REGISTER_FAILED
}
