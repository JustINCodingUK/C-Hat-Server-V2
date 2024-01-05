package io.github.justincodinguk.c_hat_server_v2.annotations.callback_annotations

/**
 * Annotated functions are called when any client, whether registered or not, connects.
 * Functions annotated with it must not declare any parameters:
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class OnClientConnect

/**
 * Annotated functions are called when a registered user connects.
 * Functions annotated with it must declare the following parameters:
 * 1. clientId: [String]
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class OnUserLogin

/**
 * Annotated functions are called when any message is received. The message body would be encrypted if isClientEncryptionAllowed is enabled in the config
 * Functions annotated with it must declare the following parameters:
 * 1. message: [io.github.justincodinguk.c_hat_server_v2.core.model.Message]
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class OnMessageReceived

/**
 * Annotated functions are called when the eventbus starts
 * Functions annotated with it must not declare any parameters:
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class OnStart

/**
 * Annotated functions are called when the eventbus is about to stop
 * Functions annotated with it must not declare any parameters:
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class OnStop

/**
 * Annotated functions are called when a registered user disconnects
 * Functions annotated with it must declare the following parameters:
 * 1. clientId: [String]
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class OnUserDisconnect

/**
 * Annotated functions are called when a registration is successful
 * Functions annotated with it must declare the following parameters:
 * 1. newUserClientId: [String]
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class OnRegisterComplete

/**
 * Annotated functions are called when a registration fails
 * Functions annotated with it must declare the following parameters:
 * 1. reason: [String]
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class OnRegisterFailed