package com.tables.plugins

import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.routing.get
import kotlinx.serialization.*


fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondRedirect("/login", false)
        }

        route("/register"){
            get{
                call.respond(HttpStatusCode.OK, "Success")
            }
            post{
                val logReq = call.receive<User>()
                if(my_queries.findByName(logReq.username) !=null){
                    call.respond(HttpStatusCode(505, "LOL"), "This user was registered")
                } // didn't work
                my_queries.addNewUser(logReq.email, logReq.username, logReq.password)
                call.respondText(JWTConfig.makeToken(logReq))
            }
        }
        route("login"){
            get{
                call.respond(HttpStatusCode.OK, "Success")
            }

            post{
                val logReq = call.receive<User>();
                val token = JWTConfig.makeToken(logReq) // create new token for user.
                println(my_queries.findByName(logReq.username))
                if(my_queries.findByName(logReq.username) == null){
                    call.respond(HttpStatusCode(505, "LOL"), "This user does not exist")
                }
                call.respond(HttpStatusCode.OK, "user: ${logReq}, JWT:${token}")
            }
        }

        authenticate{

            get("/home"){
                val user = call.principal<JWTPrincipal>()
                val username = user!!.payload.getClaim("username").asString()
                call.respond(HttpStatusCode.OK, "home for: $username")
            }

            get("/profile"){
                val user = call.principal<JWTPrincipal>()
                val username = user!!.payload.getClaim("username").asString()
                val email = user.payload.getClaim("email").asString()
                val password = user.payload.getClaim("password").asString()
                call.respond(HttpStatusCode.OK, "profile:\n $username \n $email \n $password")
            }

            get("/admin"){
                val principal = call.principal<JWTPrincipal>()
                val name = principal!!.payload.getClaim("username").asString()
                val admin = my_queries.findByName(name)
                if (admin!= null) call.respond(HttpStatusCode.OK,"admin:\n ${admin.email} \n ${admin.username}")
                // cringe without security
            }

            get("/mod"){
                val principal = call.principal<JWTPrincipal>()
                val name = principal!!.payload.getClaim("username").asString()
                val mod = my_queries.findByName(name)
                if (mod!= null) call.respond(HttpStatusCode.OK,"admin:\n ${mod.email} \n ${mod.username}")
            }

            get("/user"){
                val principal = call.principal<JWTPrincipal>()
                val username = principal!!.payload.getClaim("username").asString()
                val user = my_queries.findByName(username)
                if(user!=null) call.respond(HttpStatusCode.OK, "user:\n ${user.username} \n ${user.email}")
            }

        }

    }
}

@Serializable
data class User(
    val email: String,
    val username: String,
    val password: String
): Principal

/*
* POST http://localhost:8080/register
* JSON Body:
*{
*   "email":"pisya@popa",
*   "username": "waka",
*   "password": "boba1"
* output: *JWT token*
*} */

/*
* POST http://localhost:8080/login
* JSON Body:
*{
*   "email":"pisya@popa",
*   "username": "waka",
*   "password": "boba1"
*}
* output: "user: *user*, JWT:*token*"
*  */

/*
* GET for  http://localhost:8080/home or /profile or /admin or /mod or /user
* Authorization: Bearer {{auth_token}}
* output for /profile or /admin or /mod or /user: *data depending on the location *
* */

/*
* GET http://localhost:8080/profile
* Authorization: Bearer {{auth_token}}
* output: *data of user*
* */