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
        route("/register"){
            post{
                val logReq = call.receive<UserRegister>()
                if(my_queries.findByName(logReq.username) !=null){
                    call.respond(HttpStatusCode(505, ""), "This user was registered")
                } // didn't work
                my_queries.addNewUser(logReq.email, logReq.username, logReq.password, logReq.role, logReq.table[0])
//                call.respondText(JWTConfig.makeToken(logReq))
                val jwt = JWTConfig.makeToken(logReq)
//                call.respond(HttpStatusCode(200, "OK"), "Success")
                val json = Json.encodeToString(UserRespond(logReq.username, jwt, logReq.email, logReq.role, logReq.table))
                call.respond(json)
            }
        }
        route("/login"){

            post{
                val logReq = call.receive<UserLogin>();
//                val token = JWTConfig.makeToken(logReq) // create new token for user.
                val user = my_queries.findByName(logReq.username)
                if(user == null){
                    call.respond(HttpStatusCode(505, ""), "This user does not exist")
                }
                val userResp: UserRegister = UserRegister(user!!.username, user.email, user.password,  user.role, listOf(user.table))

//                val json = Json.encodeToString(UserRegister(user.username, user.email, user.password, user.role))
                val jwt = JWTConfig.makeToken(userResp)
                val json = Json.encodeToString(UserRespond(userResp.username, jwt, userResp.email, userResp.role, userResp.table))

//                call.respond(HttpStatusCode.OK, "user: ${logReq}, JWT:${token}")
                call.respond(json)
            }
        }

        route("/home"){
            post{
                /*
                Этот путь в authenticate засунуть нужно, просто для проверки
                работоспособности здесь оставил.
                Здесь должен обрабатываться json
                ====
                {
                    "data":"none",
                    "headers":
                        {
                            "Authorization":"Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJrdG9yLmlvIiwiZW1haWwiOiJwZXBlZ2FAZ21haWwuY29tIiwidXNlcm5hbWUiOiJwZXBlZ2EiLCJwYXNzd29yZCI6InBlcGVnYSIsInJvbGUiOiJ1c2VyIiwiYXVkIjoiaHR0cDovLzAuMC4wLjAvbG9naW4ifQ.q5VM1PPw0Vivk35x5GR5dnm6cCwjmvAyGntLHO3uVpnYF9WJMI7xGoIsPpZbN3z4wpLXPSKA4uY7D1g3ALU2Og"
                        }
                }
                ====
                data - это ключ таблички
                none - значение, название таблички, что лежит в бд у юзера, data class UserRespond, в поле table

                нужно проверить подлинность токена и call.respond вернуть json по примеру ниже,
                табличку нужно взять из бд по поиску по названию ее серилизовать в json для call.respond
                */
                val string1: TableWithMarks = TableWithMarks("1", "Что то про C", "abooba", "Ryan Gosling", "Done", "Yes", "Class A")
                val string2: TableWithMarks = TableWithMarks("2", "Что то про java", "abooba", "Pepe", "Prog", "No", "Class A")

                val table: List<TableWithMarks> = listOf(string1, string2)

                val json = Json.encodeToString(table)
                call.respond(json)
            }
        }
        route("/savetable") {
            post{
                /*
                Этот путь в authenticate засунуть нужно, просто для проверки
                работоспособности здесь оставил.
                Любой аунтентифиц. пользователь может сохранять
                Сохранение таблички в бд
                json post request:
            {"data":"[{\"id\":\"1\",\"topic\":\"Что то про C\",\"description\":\"abooba\",\"user\":\"Ryan Gosling\",\"userstatus\":\"Done\",\"checked\":\"Yes\",\"tabletitle\":\"Class A\"},{\"id\":\"2\",\"topic\":\"Что то про java\",\"description\":\"abooba\",\"user\":\"Pepe\",\"userstatus\":\"Prog\",\"checked\":\"No\",\"tabletitle\":\"Class A\"}]","headers":{"Authorization":"Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJrdG9yLmlvIiwiZW1haWwiOiJhZG1pbkBnbWFpbC5jb20iLCJ1c2VybmFtZSI6ImFkbWluIiwicGFzc3dvcmQiOiJhZG1pbiIsInJvbGUiOiJhZG1pbiIsImF1ZCI6Imh0dHA6Ly8wLjAuMC4wL2xvZ2luIn0.VvLj5iG0_CR_AjibIagAIBY6PUl4Mlw_gDwuqPYn3hpeP3T48ozsA0bNuWar0IL1eTmzABjQ0MHgA9N205yI8g"}}

             */
                call.respond(HttpStatusCode.Created,"Table Saved")
             //else
//                call.respond(HttpStatusCode.NotModified, "Error")
            }
        }

        route("/admin/changeusers"){
            post{
                /*
                Этот путь в authenticate засунуть нужно, просто для проверки
                работоспособности здесь оставил.
                Изменение полей пользователя
                json post request:
                {"data":"{role:mod,username :pepega}"}
                */
                call.respond(HttpStatusCode.OK, "Status changed")
//                else
//                  call.response(HttpStatusCode.NotModified, "Error")
            }
        }
        route("/admin/upload"){
            post{
                /*
                       Этот путь в authenticate засунуть нужно, просто для проверки
                       работоспособности здесь оставил.
                       Загрузка ексель таблички в бд
                       post request:
                       Если формат *.xls
                       -----------------------------150429568233010961794131652772
Content-Disposition: form-data; name="file"; filename="Class_A.xls"
Content-Type: application/vnd.ms-excel

<html>
  <head>
    <meta charset="UTF-8" />
  </head >
  <body>

    <table>
      <thead>
        <tr><th><b>id</b></th><th><b>topic</b></th><th><b>description</b></th><th><b>user</b></th><th><b>userstatus</b></th><th><b>checked</b></th><th><b>tabletitle</b></th></tr>
      </thead>
      <tbody>
        <tr><td>1</td><td>Что то про C</td><td>abooba</td><td>Ryan Gosling</td><td>Done</td><td>Yes</td><td>Class A</td></tr>
        <tr><td>2</td><td>Что то про java</td><td>abooba</td><td>Pepe</td><td>Prog</td><td>No</td><td>Class A</td></tr>
      </tbody>
    </table>

  </body>
</html >

-----------------------------150429568233010961794131652772--
                Если формат
                Нужно положить его в бд
                       */
                call.respond(HttpStatusCode.OK)
            }
        }




        authenticate{
//==========================IMPORTANT code example=================
//            get("/home"){
//                val user = call.principal<JWTPrincipal>()
//                val username = user!!.payload.getClaim("username").asString()
////                call.respond(HttpStatusCode.OK, "home for: $username")
//                val column1: TableWithMarks = TableWithMarks("1", "Что то про C", "abooba", "Ryan Gosling", "Done", "Yes", "Class A")
//                val column2: TableWithMarks = TableWithMarks("2", "Что то про java", "abooba", "Pepe", "Prog", "No", "Class A")
//                val testJson: List<TableWithMarks> = listOf(column1, column2)
//                val json = Json.encodeToString(testJson)
//                call.respond(json)
//            }
//-----------------------------------------------------

//            get("/profile"){
//                val user = call.principal<JWTPrincipal>()
//                val username = user!!.payload.getClaim("username").asString()
//                val email = user.payload.getClaim("email").asString()
//                val password = user.payload.getClaim("password").asString()
//                call.respond(HttpStatusCode.OK, "profile:\n $username \n $email \n $password")
//            }


            get("/admin"){
                val principal = call.principal<JWTPrincipal>()
//                val name = principal!!.payload.getClaim("username").asString()
//                val admin = my_queries.findByName(name)
//                if (admin!= null) {

//                    call.respond(HttpStatusCode.OK,"admin:\n ${admin.email} \n ${admin.username}")
                    val allUsers = my_queries.allUsers()
                    print(allUsers) // [Test(username=admin, email=admin@gmail.com, password=admin, role=admin, table=all), Test(username=pepega, email=pepega@gmail.com, password=pepega, role=user, table=none), Test(username=test3, email=test@gmail.com, password=test, role=user, table=none), Test(username=test4, email=test@gmail.com, password=test, role=user, table=none), Test(username=test6, email=test@gmail.com, password=test, role=user, table=none)]
                    val json = Json.encodeToString(allUsers)
                    call.respond(json)
//                }
//                else {
//                    call.respond(HttpStatusCode.Locked)
//                }
            }


//            get("/admin/tables"){
//                val principal = call.principal<JWTPrincipal>()
//                val name = principal!!.payload.getClaim("username").asString()
//                val admin = my_queries.findByName(name)
//                if (admin!= null) {
//                    call.respond(HttpStatusCode.OK,table_queres.allTables())
//                }
//                else call.respond(HttpStatusCode.Locked)
//            }

//            post("/admin/table"){
//                ////////////////////////////////////////////////////////
//                val table = call.receive<TableWithMarks>()
//
//                val principal = call.principal<JWTPrincipal>()
//                val name = principal!!.payload.getClaim("username").asString()
//                val admin = my_queries.findByName(name)
//                table_queres.addNewTable(table.id, table.topic, table.description, table.userstatus, table.checked, table.tabletitle)
//
//                if (admin!= null) {
//                    call.respond(HttpStatusCode.OK,"This table was written!")
//                }
//
//                else call.respond(HttpStatusCode.Locked)
//            }

            get("/mod"){
                /*
                Модератор как админ, только проверка на модератора
                 */
                val principal = call.principal<JWTPrincipal>()
//                val name = principal!!.payload.getClaim("username").asString()
//                val mod = my_queries.findByName(name)
//                if (mod!= null) call.respond(HttpStatusCode.OK,"admin:\n ${mod.email} \n ${mod.username}")
                val allUsers = my_queries.allUsers()
                print(allUsers) // [Test(username=admin, email=admin@gmail.com, password=admin, role=admin, table=all), Test(username=pepega, email=pepega@gmail.com, password=pepega, role=user, table=none), Test(username=test3, email=test@gmail.com, password=test, role=user, table=none), Test(username=test4, email=test@gmail.com, password=test, role=user, table=none), Test(username=test6, email=test@gmail.com, password=test, role=user, table=none)]
                val json = Json.encodeToString(allUsers)
                call.respond(json)

            }

//            get("/user"){
//                val principal = call.principal<JWTPrincipal>()
//                val username = principal!!.payload.getClaim("username").asString()
//                val user = my_queries.findByName(username)
//                if(user!=null) call.respond(HttpStatusCode.OK, "user:\n ${user.username} \n ${user.email}")
//            }

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