package com.tables.plugins

import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import org.jetbrains.exposed.sql.transactions.experimental.*



object DatabaseFactory {
    fun init(){
        val usersDB = Database.connect(
            "jdbc:sqlite:src/main/kotlin/com/tables/db/users.sqlite3", driver = "org.sqlite.JDBC",
            user = "admin", password = "secret")
        transaction(usersDB) {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(UsersDB)
            //commit() do stuff what?
            //Do stuff
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}


data class Test(val username: String, val email: String, val password: String, val role: String)

object UsersDB : Table() {
    // fields of table: id title and body?
    //val id = integer("id").autoIncrement()
    val username = varchar("username", 128)
    val email = varchar("email", 1024)
    val password = varchar("password", 1024)
    val role = varchar("role", 128)

    //override val primaryKey = PrimaryKey(id)
}
