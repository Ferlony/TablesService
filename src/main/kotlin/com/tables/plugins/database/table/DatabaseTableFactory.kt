package com.tables.plugins.database.table

import com.tables.plugins.database.client.BaseForUsers.varchar
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import org.jetbrains.exposed.sql.transactions.experimental.*

object DatabaseTableFactory {
    fun init(){
        val testDB = Database.connect(
            "jdbc:sqlite:src/main/kotlin/com/tables/db/tables.sqlite3", driver = "org.sqlite.JDBC",
            user = "admin", password = "secret")
        transaction(testDB) {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(BaseForTables)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}


data class Tables(val id: String, val topic: String, val description: String, val userstatus: String, val checked: String, val tabletittle: String)

object BaseForTables : Table() {
    val id = varchar("email", 1024)
    val topic = varchar("username", 128)
    val userstatus = varchar("password", 1024)
    val description = varchar("password", 1024)
    val checked = varchar("password", 1024)
    val tabletittle = varchar("password", 1024)
}