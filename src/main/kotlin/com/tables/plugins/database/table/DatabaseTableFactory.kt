package com.tables.plugins.database.table

import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import org.jetbrains.exposed.sql.transactions.experimental.*

object DatabaseTableFactory {
    fun init(){
        val testDB = Database.connect(
            "jdbc:sqlite:src/main/kotlin/com/tables/db/tables2.sqlite3", driver = "org.sqlite.JDBC",
            user = "admin", password = "secret")
        transaction(testDB) {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(BaseForTables)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}

@Serializable
data class Tables(val id: String, val topic: String, val description: String, val userstatus: String, val checked: String, val tabletittle: String)

object BaseForTables : Table() {
    val id = varchar("id", 1024)
    val topic = varchar("topic", 128)
    val userstatus = varchar("userstatus", 1024)
    val description = varchar("description", 1024)
    val checked = varchar("checked", 1024)
    val tabletittle = varchar("tabletittle", 1024)
}