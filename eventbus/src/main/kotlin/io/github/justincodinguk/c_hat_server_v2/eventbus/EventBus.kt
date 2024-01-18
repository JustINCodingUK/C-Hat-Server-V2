package io.github.justincodinguk.c_hat_server_v2.eventbus

import kotlinx.coroutines.launch

class EventBus {

    private val eventBusScope = EventBusCoroutineScope()
    private val eventToFunctionMap = mutableMapOf<Event, MutableList<OnEventCallback>>()

    fun registerTask(event: Event, task: OnEventCallback) {
        if(eventToFunctionMap[event] == null) {
            eventToFunctionMap[event] = mutableListOf()
        }
        eventToFunctionMap[event]!!.add(task)
    }

    fun registerAll(event: Event, tasks: List<OnEventCallback>) {
        if(eventToFunctionMap[event] == null) {
            eventToFunctionMap[event] = mutableListOf()
        }
        eventToFunctionMap[event]!!.addAll(tasks)
    }

    fun fireEvent(event: Event, eventObject: Any? = null) {
        if(eventToFunctionMap[event] != null) {
            for(task in eventToFunctionMap[event]!!) {
                try {
                    eventBusScope.launch {
                        println("Calling ${task.callback.name}, on ${task.classInstance.javaClass.name}")
                        task.callback(task.classInstance, eventObject)
                        //task.invokeCallback(eventObject)
                    }
                } catch (e: Exception) {
                    throw IllegalStateException("EventBus functions cannot be class members")
                }
            }
        }
    }

    fun unregisterCallbacksFor(pluginId: String) {
        eventToFunctionMap.values.forEach { eventCallback ->
            eventCallback.removeIf { it.pluginId == pluginId }
        }
    }

    fun start() = fireEvent(Event.ON_START)

    fun stop() {
        eventBusScope.cancel()
        fireEvent(Event.ON_STOP)
    }
}