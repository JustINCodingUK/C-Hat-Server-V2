package io.github.justincodinguk.c_hat_server_v2.eventbus

class EventBus {

    private val eventToFunctionMap = mutableMapOf<Event, MutableList<() -> Unit>>()

    fun registerTask(event: Event, task: () -> Unit) {
        if(eventToFunctionMap[event] == null) {
            eventToFunctionMap[event] = mutableListOf()
        }
        eventToFunctionMap[event]!!.add(task)
    }

    fun registerAll(event: Event, tasks: List<() -> Unit>) {
        if(eventToFunctionMap[event] == null) {
            eventToFunctionMap[event] = mutableListOf()
        }
        eventToFunctionMap[event]!!.addAll(tasks)
    }

    fun fireEvent(event: Event) {
        if(eventToFunctionMap[event] != null) {
            for(task in eventToFunctionMap[event]!!) {
                try {
                    task()
                } catch (e: Exception) {
                    throw IllegalStateException("EventBus functions cannot be class members")
                }
            }
        }
    }

    fun start() = fireEvent(Event.ON_START)

    fun stop() = fireEvent(Event.ON_STOP)
}