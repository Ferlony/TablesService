package com.tables

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.tables.plugins.*


fun main() {
    embeddedServer(Netty, port = 8050, host = "localhost", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    DatabaseFactory.init()
    configureSecurity()
    configureRouting()
}
