package io.github.justincodinguk.c_hat_server_v2.annotations

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class PluginMain(val pluginId: String)