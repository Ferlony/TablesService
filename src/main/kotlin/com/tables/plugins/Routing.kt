package com.tables.plugins

import com.auth0.jwt.JWT
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.util.*


//var users = ArrayList<User>()

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondRedirect("/login", false)
        }

        post("/register"){
            val logReq = call.receive<User>();
            // check, have sql table this user
            if(call.parameters.getOrFail<String>(logReq.username).isNotEmpty()
                && my_queries.test(call.parameters.getOrFail<Int>("id"))!=null){
                call.respondRedirect("/login", false); // ?????? mb cringe
                call.respond(HttpStatusCode(505, "LOL"), "This user was registered")
            }
            val article = my_queries.addNewTest(logReq.username, logReq.password)
            call.respondRedirect("/profile/${article?.id}", true)
            //call.respond(HttpStatusCode.Created, "Success")
        }

        get("/login"){
            val logReq = call.receive<User>();
            val token = JWT.create() // maybe
            if(call.parameters.getOrFail<String>(logReq.username).isNotEmpty()){
                call.respond(hashMapOf("token" to token))
                //call.respondRedirect("/profile/${logReq.id}", true);
            }
            else{
                call.respondRedirect("/register", false);
            }
        }

        ///
        authenticate() {//cringe
            get("/profile") {
                val principal = call.principal<JWTPrincipal>()
                val username = principal!!.payload.getClaim("username").asString()
                val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                call.respond(HttpStatusCode.OK, "success")
            }
        }
        ///

        route("/profile"){
            get("{id}") {
                val id = call.parameters.getOrFail<Int>("id").toInt() // cringe
                val user = my_queries.test(id)
                if (user != null) {
                    call.respond(HttpStatusCode.OK, user)
                } // cringe
                // TODO
            }
        }

        get("/admin"){
            //TODO
        }

        get("/mod"){
            //TODO
        }
        get("/user/{id}"){
            val user = my_queries.test(call.parameters.getOrFail<Int>("id").toInt())
            if(user!= null){
                call.respond(HttpStatusCode.Accepted, user)
            }
            else {
                call.respond(HttpStatusCode.BadRequest, "This user is not exist")
            }
            // maybe not cringe
        }


    }
}


data class User(
    val username: String,
    val password: String,
    val id : Int
)

data class Response(
    val ok: Boolean,
    val message: String
)
