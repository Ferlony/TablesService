package com.tables.plugins.database.table

import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class DatabaseTable : IDatabaseTable {

    private fun resultRowToTable(row: ResultRow) = Tables(
        id = row[BaseForTables.id],
        tabletittle = row[BaseForTables.tabletittle],
        data = row[BaseForTables.data]
    )

    override suspend fun allTables(): List<Tables> = DatabaseTableFactory.dbQuery {
        BaseForTables.selectAll().map(::resultRowToTable)
    }

    override suspend fun fullTable(tibletittle: String): Tables?= DatabaseTableFactory.dbQuery {
        BaseForTables
            .select(BaseForTables.tabletittle eq tibletittle)
            .map(::resultRowToTable)
            .singleOrNull()
    }
    override suspend fun addNewTable(
        id: String,
        tabletittle: String,
        data: String
    ): Tables? = DatabaseTableFactory.dbQuery {
        val insertTable = BaseForTables.insert {
            it[BaseForTables.id] = id
            it[BaseForTables.tabletittle] = tabletittle
            it[BaseForTables.data] = data
        }
        insertTable.resultedValues?.singleOrNull()?.let (::resultRowToTable )
    }

    override suspend fun editTable(
        id: String,
        tabletittle: String,
        data: String
    ): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTable(
        id: String,
        tabletittle: String,
        data: String): Boolean = DatabaseTableFactory.dbQuery {
        TODO("Not yet implemented")
    }



}
val table_queres: IDatabaseTable = DatabaseTable().apply {
    runBlocking {
        if(allTables().isEmpty()){
            addNewTable("0", "Class A",
                "{\"data\":[{" + // data = {TableWithMarks.kt}
                    "\"id\": \"1\"," +
                    "\"topic\": \"Что то про С\"," +
                    "\"description\": \"aboba\"," +
                    "\"user\": \"Ryan Gosling\"," +
                    "\"userstatus\": \"Done\"," +
                    "\"checked\":\"Yes\"," +
                    "\"tabletitle\":\"Class A\"" +
                    "},{" +
                    "\"id\": \"2\"," +
                    "\"topic\": \"Что то про Java\"," +
                    "\"description\": \"aboba\"," +
                    "\"user\": \"Pepe\"," +
                    "\"userstatus\": \"Progress\"," +
                    "\"checked\":\"Yes\"," +
                    "\"tabletitle\":\"Class A\"" +
                    "}]}")
        }
    }
}