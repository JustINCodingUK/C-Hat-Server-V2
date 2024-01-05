package io.github.justincodinguk.c_hat_server_v2.core

import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.SimpleEmail

fun generateEmail(message: String, recipient: String): SimpleEmail {
    val email = SimpleEmail()
    email.apply {
        hostName = "smtp-mail.outlook.com"
        setAuthenticator(DefaultAuthenticator(System.getenv("C_HAT_EMAIL"), System.getenv("C_HAT_PASSWORD")))
        isSSLOnConnect = false
        isStartTLSEnabled = true
        setFrom(System.getenv("C_HAT_EMAIL"))
        subject = "C-Hat Account Verification"
        setMsg(message)
        addTo(recipient)
    }

    return email
}