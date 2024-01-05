package io.github.justincodinguk.c_hat_server_v2

import io.github.justincodinguk.c_hat_server_v2.annotations.callback_annotations.*
import io.github.justincodinguk.c_hat_server_v2.eventbus.Event

enum class ParameterType {
    MESSAGE,
    STRING,
    NO_PARAM
}

val annotationClassToEventMap = mapOf(
    OnStart::class.java to Event.ON_START,
    OnStop::class.java to Event.ON_STOP ,
    OnUserLogin::class.java to Event.ON_USER_LOGIN,
    OnClientConnect::class.java to Event.ON_CLIENT_CONNECT,
    OnRegisterFailed::class.java to Event.ON_REGISTER_FAILED,
    OnMessageReceived::class.java to Event.ON_MESSAGE_RECEIVED,
    OnUserDisconnect::class.java to Event.ON_USER_DISCONNECT,
    OnRegisterComplete::class.java to Event.ON_REGISTER_COMPLETE
)

val eventToParamMap = mapOf(
    Event.ON_START to ParameterType.NO_PARAM,
    Event.ON_STOP to ParameterType.NO_PARAM,
    Event.ON_CLIENT_CONNECT to ParameterType.NO_PARAM,
    Event.ON_USER_LOGIN to ParameterType.STRING,
    Event.ON_REGISTER_FAILED to ParameterType.STRING,
    Event.ON_USER_DISCONNECT to ParameterType.STRING,
    Event.ON_REGISTER_COMPLETE to ParameterType.STRING,
    Event.ON_MESSAGE_RECEIVED to ParameterType.MESSAGE
)