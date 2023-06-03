package com.tables.plugins

import com.tables.plugins.database.client.my_queries
import com.tables.plugins.database.table.table_queres
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.routing.get
import kotlinx.serialization.*
import kotlinx.serialization.json.Json


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
                val logReq = call.receive<UserRegister>()
                if(my_queries.findByName(logReq.username) !=null){
                    call.respond(HttpStatusCode(505, ""), "This user was registered")
                } // didn't work
                my_queries.addNewUser(logReq.email, logReq.username, logReq.password, logReq.role)
//                call.respondText(JWTConfig.makeToken(logReq))
                val jwt = JWTConfig.makeToken(logReq)
//                call.respond(HttpStatusCode(200, "OK"), "Success")
                val json = Json.encodeToString(UserRespond(logReq.username, jwt, "0", logReq.email, logReq.role))
                call.respond(json)
            }
        }
        route("login"){
            get{
                call.respond(HttpStatusCode.OK, "Success")
            }

            post{
                val logReq = call.receive<UserLogin>();
//                val token = JWTConfig.makeToken(logReq) // create new token for user.
                val user = my_queries.findByName(logReq.username)
                if(user == null){
                    call.respond(HttpStatusCode(505, ""), "This user does not exist")
                }
                val userClass: UserRegister = UserRegister(user!!.username, user.email, user.password,  user.role)

//                val json = Json.encodeToString(UserRegister(user.username, user.email, user.password, user.role))
                val jwt = JWTConfig.makeToken(userClass)
                val json = Json.encodeToString(UserRespond(userClass.username, jwt, "0", userClass.email, userClass.role))

//                call.respond(HttpStatusCode.OK, "user: ${logReq}, JWT:${token}")
                call.respond(json)
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
                else call.respond(HttpStatusCode.Locked)
                // cringe without security
            }

            get("/admin/tables"){
                val principal = call.principal<JWTPrincipal>()
                val name = principal!!.payload.getClaim("username").asString()
                val admin = my_queries.findByName(name)
                if (admin!= null) {
                    call.respond(HttpStatusCode.OK,table_queres.allTables())
                }
                else call.respond(HttpStatusCode.Locked)
            }

            post("/admin/table"){
                ////////////////////////////////////////////////////////
                val table = call.receive<TableWithMarks>()

                val principal = call.principal<JWTPrincipal>()
                val name = principal!!.payload.getClaim("username").asString()
                val admin = my_queries.findByName(name)
                table_queres.addNewTable(table.id, table.topic, table.description, table.userstatus, table.checked, table.tabletittle)

                if (admin!= null) {
                    call.respond(HttpStatusCode.OK,"This table was written!")
                }

                else call.respond(HttpStatusCode.Locked)
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