package com.tables

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.tables.plugins.*
import com.tables.plugins.database.client.DatabaseFactory
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

fun main() {
    embeddedServer(Netty, port = 8080, host = "localhost", module = Application::module)
        .start(wait = true)
}


fun Application.module() {
    DatabaseFactory.init()
    install(ContentNegotiation){
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }

    configureSecurity()
    configureRouting()
}
