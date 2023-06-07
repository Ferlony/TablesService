package com.tables.plugins.database.table

import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class DatabaseTable : IDatabaseTable {

    private fun resultRowToTable(row: ResultRow) = Tables(
        id = row[BaseForTables.id],
        topic = row[BaseForTables.topic],
        description = row[BaseForTables.description],
        userstatus = row[BaseForTables.userstatus],
        checked = row[BaseForTables.checked],
        tabletittle = row[BaseForTables.tabletittle]
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
        topic: String,
        description: String,
        userstatus: String,
        checked: String,
        tabletittle: String
    ): Tables? = DatabaseTableFactory.dbQuery {
        val insertTable = BaseForTables.insert {
            it[BaseForTables.id] = id
            it[BaseForTables.topic] = topic
            it[BaseForTables.description] = description
            it[BaseForTables.userstatus] = userstatus
            it[BaseForTables.checked] = checked
            it[BaseForTables.tabletittle] = tabletittle
        }
        insertTable.resultedValues?.singleOrNull()?.let (::resultRowToTable )
    }

    override suspend fun editTable(
        id: String,
        topic: String,
        description: String,
        userstatus: String,
        checked: String,
        tabletittle: String
    ): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTable(id: String, topic: String): Boolean = DatabaseTableFactory.dbQuery {
        TODO("Not yet implemented")
    }



}
val table_queres: IDatabaseTable = DatabaseTable().apply {
    runBlocking {
        if(allTables().isEmpty()){
            addNewTable("0", "null", "test", "Done", "No", "Class NULL")
            addNewTable("1", "null1", "test1", "Done1", "No1", "Class NULL1")
            addNewTable("2", "null2", "test2", "Done2", "No2", "Class NULL2")
            addNewTable("3", "null3", "test3", "Done3", "No3", "Class NULL3")

        }
    }
}