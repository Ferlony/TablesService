package com.tables.plugins

import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import javax.security.auth.login.Configuration.Parameters

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        post("/") {
            val loginRequest = call.receive<TestClass>()

            if (loginRequest.email == "admin" && loginRequest.password == "admin") {
                print("Email: ${loginRequest.email}\n" + "Password: ${loginRequest.password}")
                call.respond(TestResponse(true, "Login successful!"))
            }
            else {
                print("Email: ${loginRequest.email}\n" + "Password: ${loginRequest.password}")
                call.respond(TestResponse(false, "Invalid password!"))
            }
        }
    }
}


data class TestClass(
    val email: String,
    val password: String
)

data class TestResponse(
    val ok: Boolean,
    val message: String
)
