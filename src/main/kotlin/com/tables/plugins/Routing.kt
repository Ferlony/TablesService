package com.tables.plugins

import com.tables.plugins.database.client.my_queries
import com.tables.plugins.database.table.Tables
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
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.util.*
import kotlin.collections.HashMap
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

suspend fun allTables(): List<Tables> {

    return table_queres.allTables()
}

fun isNumeric(s: String): Boolean {
    return s.chars().allMatch { Character.isDigit(it) }
}

fun Application.configureRouting() {
    routing {
        route("/register"){
            post{
                val logReq = call.receive<UserRegister>()
                if(my_queries.findByName(logReq.username) !=null){
                    call.respond(HttpStatusCode(505, "LOL"), "This user was registered")
                }
                my_queries.addNewUser(logReq.email, logReq.username, logReq.password, logReq.role, logReq.table.toString())
                val jwt = JWTConfig.makeToken(logReq)
                val json = Json.encodeToString(UserRespond(logReq.username, jwt, logReq.email, logReq.role, logReq.table))
                call.respond(json)
            }
        }
        route("/login"){

            post{
                val logReq = call.receive<UserLogin>()
                val user = my_queries.findByName(logReq.username)
                if(user == null){
                    call.respond(HttpStatusCode(505, "LOL"), "This user does not exist")
                }

                val userResp: UserRegister = UserRegister(user!!.username, user.email, user.password,  user.role, listOf(user.table))
                if(user.role == "admin" || user.role == "moderator"){
                    val alltables = allTables()
                    val stables : LinkedList<String> = LinkedList()
                    alltables.forEach {
                        stables.add(it.toString())
                    }

                    val jwt = JWTConfig.makeToken(userResp)
                    val json = Json.encodeToString(UserRespond(userResp.username, jwt, userResp.email, userResp.role, stables))
                    call.respond(json)
                }
                val jwt = JWTConfig.makeToken(userResp)
                val json = Json.encodeToString(UserRespond(userResp.username, jwt, userResp.email, userResp.role, userResp.table))
                call.respond(json)
            }
        }





        authenticate{
            route("/home"){
                post{
                    val prinicipal = call.principal<JWTPrincipal>()
                    val username = prinicipal!!.payload.getClaim("username").asString()
                    val user = my_queries.findByName(username)
                    val logReq =call.receive<String>()
                    val jsonFormat = Json.decodeFromString<Tables>(logReq)
                    if(user!=null){
                        if(user.table == "all") {
                            val tables: List<Tables> = table_queres.allTables()
                            val json = Json.encodeToString(tables)
                            call.respond(json) // возвращает все таблицы для модератора или админа
                        }
                        else {
                            println(table_queres.fullTable(jsonFormat.tabletittle))
                            val table = table_queres.fullTable(jsonFormat.tabletittle)
                            if(table!= null) {
                                val json = Json.encodeToString(table)
                                call.respond(json)
                            }
                        }
                    }
                    /*val jsonFormat = Json.decodeFromString<Data>(logReq)

                    if(user != null && (user.role == "admin" || user.role == "moderator")){
//                        if(jsonFormat.data == "all") {
//                            val tables: List<Tables> = table_queres.allTables()
//                            val json = Json.encodeToString(tables)
//                            call.respond(json) // возвращает все таблицы для модератора или админа
//                        }
//                        else{
                        println(table_queres.fullTable(jsonFormat.data))
                        val table = table_queres.fullTable(jsonFormat.data)
                        if(table!= null) {
                            val json = Json.encodeToString(table)
                            call.respond(json)
                        }
                        else call.respond(HttpStatusCode.NotFound)
//                      }
                    }
                    if (user != null && user.role =="user"){
                        var table: Tables? = table_queres.fullTable(user.table) // поиск по названию таблицы.
                        // пользователь состоит в одной таблице или нескольких?
                        if(table==null) {
                            table = table_queres.fullTable(jsonFormat.data)
                            val json = Json.encodeToString(table)
                            call.respond(json)
                        }
                        val json = Json.encodeToString(table)
                        call.respond(json) // возвращает таблицу в которой записан пользователь
                    }
                    else {
                        call.respond(HttpStatusCode.BadRequest)
                    }*/

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
                    var logReq = call.receive<String>()
                    //val regex = "<tbody>.+</tbody>".toRegex()
                    logReq = logReq
                        .substringBefore("</tbody>")
                        .substringAfter("<tbody>")
                        .trim()
                        .replace("<td>", "").replace("</td>", ":").replace("<tr>", "").replace("</tr>", "")
                    logReq +="\n0:"
                    var string = ""
                    val s: LinkedList<TableWithMarks> = LinkedList()
                    val ss:Array<String> = arrayOf("","","","","","","")
                    var count = 0
                    logReq.forEach {
                        if(it==':') {
                            string = string.trim()
                            if(isNumeric(string)){
                                count = 0
                                if(ss.get(1)!="") s.add(TableWithMarks(ss.get(0),ss.get(1),ss.get(2),ss.get(3),ss.get(4),ss.get(5),ss.get(6)))
                            }
                            ss.set(count, string)
                            count+=1
                            string = ""
                        }
                        else string+=it
                    }
                    s.forEach{
                        table_queres.addNewTable(it.id, it.topic, it.description, it.userstatus, it.checked, it.tabletitle)
                        val user = my_queries.findByName(it.user)
                        my_queries.editUserTable(user!!.username, it.tabletitle)
                    }

                    call.respond(HttpStatusCode.OK)

                }
            }

            route("/savetable") {
                post{
                    /*
                    Этот путь в authenticate засунуть нужно, просто для проверки
                    работоспособности здесь оставил.
                    Любой аунтентифиц. пользователь может сохранять таблицу
                    Сохранение таблички в бд
                    json post request:
                {"data":"[{\"id\":\"1\",\"topic\":\"Что то про C\",\"description\":\"abooba\",\"user\":\"Ryan Gosling\",\"userstatus\":\"Done\",\"checked\":\"Yes\",\"tabletitle\":\"Class A\"},{\"id\":\"2\",\"topic\":\"Что то про java\",\"description\":\"abooba\",\"user\":\"Pepe\",\"userstatus\":\"Prog\",\"checked\":\"No\",\"tabletitle\":\"Class A\"}]","headers":{"Authorization":"Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJrdG9yLmlvIiwiZW1haWwiOiJhZG1pbkBnbWFpbC5jb20iLCJ1c2VybmFtZSI6ImFkbWluIiwicGFzc3dvcmQiOiJhZG1pbiIsInJvbGUiOiJhZG1pbiIsImF1ZCI6Imh0dHA6Ly8wLjAuMC4wL2xvZ2luIn0.VvLj5iG0_CR_AjibIagAIBY6PUl4Mlw_gDwuqPYn3hpeP3T48ozsA0bNuWar0IL1eTmzABjQ0MHgA9N205yI8g"}}
                 */
                    val prinicipal = call.principal<JWTPrincipal>()
                    val username = prinicipal!!.payload.getClaim("username").asString()
                    val user = my_queries.findByName(username)
                    val logReq = call.receive<String>()
                    var json = Json.parseToJsonElement(logReq)
                    var data = json.jsonObject.get("data").toString() // запрос идет именно так:  {"data":"[{\"id\":\"1\",\"topic\":\"Что то про C\",\"description\":\"abooba\".....
                    data = data.replace("\\", "")
                    data = data.substring(1, data.length-1)
                    data = "{\"data\":$data}"
                    val s : DataTable = Json.decodeFromString(DataTable.serializer(), data)
                    s.data.forEach{//добваление а не сохранение таблички, нужно будет переделать, а так работает, он добавляет каждую из таблиц в БД
                        table_queres.addNewTable(it.id, it.topic, it.userstatus, it.description, it.checked, it.tabletitle)
                    }
                    //my_queries.editUserTable(user.username, )
                    call.respond(HttpStatusCode.Created,"Table Saved")
                }
            }


            get("/admin"){
                val principal = call.principal<JWTPrincipal>()
                val name = principal!!.payload.getClaim("username").asString()
                val admin = my_queries.findByName(name)
                if (admin!= null && admin.role == "admin") {
                    val allUsers = my_queries.allUsers()
                    print(allUsers) // [Test(username=admin, email=admin@gmail.com, password=admin, role=admin, table=all), Test(username=pepega, email=pepega@gmail.com, password=pepega, role=user, table=none), Test(username=test3, email=test@gmail.com, password=test, role=user, table=none), Test(username=test4, email=test@gmail.com, password=test, role=user, table=none), Test(username=test6, email=test@gmail.com, password=test, role=user, table=none)]
                    val json = Json.encodeToString(allUsers)
                    call.respond(json)
                }
                else {
                    call.respond(HttpStatusCode.Locked)
                }
            }

            post("/admin/changeusers"){
                //val principal = call.principal<JWTPrincipal>()
                //val admin = my_queries.findByName(principal!!.payload.getClaim("username").asString())
                //if (admin!= null && admin.role == "admin") {
                    val jsonencode = call.receive<String>()
                    val json = Json.parseToJsonElement(jsonencode)
                    val data = json.jsonObject.get("data").toString() // "{role:mod,username: pepega}"
                    var userData = ""
                    val linkdata : LinkedList<String> = LinkedList()
                    data.forEach {
                        if(it != '{' && it != '"' && it != '}' && it !=':'){
                            userData+=it
                        } else {
                            if(it == ':' || it =='}') linkdata.add(userData)
                            userData=""
                        }

                    } // последний элемент ВСЕГДА должен быть username и его нельзя менять, либо нельзя менять почту, но этого я не сделал!
                    val username = my_queries.findByName(linkdata.last.replace("\\s+".toRegex(), ""))
                    if(username == null)   call.respond(HttpStatusCode.NotModified, "Error")
                    my_queries.editUser(linkdata.last.replace("\\s+".toRegex(), ""), linkdata.get(1).replace("\\s+".toRegex(), ""))
                    call.respond(HttpStatusCode.OK, "Status changed")
                //}
                //else {
                //    call.respond(HttpStatusCode.Locked)
                //}
                /*
                Этот путь в authenticate засунуть нужно, просто для проверки
                работоспособности здесь оставил.
                Изменение полей пользователя
                json post request:
                {"data":"{role:mod,username: pepega}"}
                */
            }


            get("/mod"){
                /*
                Модератор как админ, только проверка на модератора
                 */
                val principal = call.principal<JWTPrincipal>()
                val name = principal!!.payload.getClaim("username").asString()
                val mod = my_queries.findByName(name)
                if(mod != null && mod.role == "moderator") {
                    val allUsers = my_queries.allUsers()
                    print(allUsers) // [Test(username=admin, email=admin@gmail.com, password=admin, role=admin, table=all), Test(username=pepega, email=pepega@gmail.com, password=pepega, role=user, table=none), Test(username=test3, email=test@gmail.com, password=test, role=user, table=none), Test(username=test4, email=test@gmail.com, password=test, role=user, table=none), Test(username=test6, email=test@gmail.com, password=test, role=user, table=none)]
                    val json = Json.encodeToString(allUsers)
                    call.respond(json)
                }
                else {
                    call.respond(HttpStatusCode.Locked)
                }

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