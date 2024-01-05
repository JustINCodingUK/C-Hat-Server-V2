package io.github.justincodinguk.c_hat_server_v2.eventbus

import io.github.justincodinguk.c_hat_server_v2.eventbus.context.Context

class EventBus {

    private val eventToFunctionMap = mutableMapOf<Event, MutableList<(Any?) -> Unit>>()

    fun registerTask(event: Event, task: (Any?) -> Unit) {
        if(eventToFunctionMap[event] == null) {
            eventToFunctionMap[event] = mutableListOf()
        }
        eventToFunctionMap[event]!!.add(task)
    }

    fun registerAll(event: Event, tasks: List<(Any?) -> Unit>) {
        if(eventToFunctionMap[event] == null) {
            eventToFunctionMap[event] = mutableListOf()
        }
        eventToFunctionMap[event]!!.addAll(tasks)
    }

    fun fireEvent(event: Event, eventObject: Any? = null) {
        if(eventToFunctionMap[event] != null) {
            for(task in eventToFunctionMap[event]!!) {
                try {
                    task(eventObject)
                } catch (e: Exception) {
                    throw IllegalStateException("EventBus functions cannot be class members")
                }
            }
        }
    }

    fun start() = fireEvent(Event.ON_START)

    fun stop() = fireEvent(Event.ON_STOP)
}