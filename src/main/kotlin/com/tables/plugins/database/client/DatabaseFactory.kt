package com.tables.plugins.database.client

import com.tables.plugins.database.table.BaseForTables
import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import org.jetbrains.exposed.sql.transactions.experimental.*



object DatabaseFactory {
    fun init(){
        val usersDB = Database.connect(
            "jdbc:sqlite:src/main/kotlin/com/tables/db/users2.sqlite3", driver = "org.sqlite.JDBC",
            user = "admin", password = "secret")
        transaction(usersDB) {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(Users)
            SchemaUtils.create(BaseForTables)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}

@Serializable
data class Test(val username: String, val email: String, val password: String, val role: String, val table: String)

object Users : Table() {
    val username = varchar("username", 1024)
    val email = varchar("email", 1024)
    val password = varchar("password", 1024)
    val role = varchar("role", 1024)
    val table = varchar("table", 1024)
}
