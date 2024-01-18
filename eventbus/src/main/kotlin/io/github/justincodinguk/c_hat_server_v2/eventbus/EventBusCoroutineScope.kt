package io.github.justincodinguk.c_hat_server_v2.eventbus

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class EventBusCoroutineScope : CoroutineScope {
    private val job = Job()

    override val coroutineContext = Dispatchers.Unconfined + job

    fun cancel() = job.cancel()
}