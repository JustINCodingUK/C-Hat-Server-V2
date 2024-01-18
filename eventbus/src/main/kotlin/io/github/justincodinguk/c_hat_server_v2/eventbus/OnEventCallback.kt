package io.github.justincodinguk.c_hat_server_v2.eventbus

import java.lang.reflect.Method

data class OnEventCallback(
    val pluginId: String,
    val callback: Method,
    val classInstance: Any
) {
    fun invokeCallback(eventObj: Any?) {
        if(eventObj==null) {
            callback.invoke(classInstance)
        } else {
            callback.invoke(classInstance, eventObj)
        }
    }
}

