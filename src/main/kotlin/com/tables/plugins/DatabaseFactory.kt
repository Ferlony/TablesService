package com.tables.plugins

import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import org.jetbrains.exposed.sql.transactions.experimental.*



object DatabaseFactory {
    fun init(){
        val testDB = Database.connect(
            "jdbc:sqlite:src/main/kotlin/com/tables/db/test.sqlite3", driver = "org.sqlite.JDBC",
            user = "admin", password = "secret")
        transaction(testDB) {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(Tests)
            //commit() do stuff what?
            //Do stuff
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}


data class Test(val id: Int, val username: String, val password: String)

object Tests : Table() {
    // fields of table: id title and body?
    val id = integer("id").autoIncrement()
    val username = varchar("title", 128)
    val password = varchar("body", 1024)

    override val primaryKey = PrimaryKey(id)
}
