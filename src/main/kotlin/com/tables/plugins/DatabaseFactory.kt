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
            //commit()
            //Do stuff
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}


data class Test(val id: Int, val title: String, val body: String)

object Tests : Table() {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 128)
    val body = varchar("body", 1024)

    override val primaryKey = PrimaryKey(id)
}
