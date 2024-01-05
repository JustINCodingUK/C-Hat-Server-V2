package io.github.justincodinguk.c_hat_server_v2.core.config

import io.github.justincodinguk.c_hat_server_v2.core.serializers.DateFormatSerializer
import io.ktor.server.engine.*
import kotlinx.serialization.Serializable
import java.nio.file.Path
import java.text.SimpleDateFormat

@Serializable
data class Config(
    val port: Int,
    val host: String,
    val isDebugModeEnabled: Boolean,
    val defaultHostOnDebug: Boolean,
    val isEncryptDbEnabled: Boolean,

    @Serializable(with = DateFormatSerializer::class)
    val dateTimeFormat: SimpleDateFormat,

    val logFilePath: Path,
    val isClientEncryptionAllowed: Boolean,
    val isEmailAuthEnabled: Boolean,
    val jvmArgs: String,
)