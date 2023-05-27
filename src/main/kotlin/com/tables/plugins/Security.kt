package com.tables.plugins

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.application.*

fun Application.configureSecurity() {
    
    authentication {
            jwt {
                verifier(JWTConfig.verifier)
                realm = "ktor.io"
                validate { credential ->
                    if (credential.payload.audience.contains(JWTConfig.audience)) JWTPrincipal(credential.payload) else null
                }

            }
        }

}
