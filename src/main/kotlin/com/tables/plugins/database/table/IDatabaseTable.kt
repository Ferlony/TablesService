package com.tables.plugins.database.table


interface IDatabaseTable {
    suspend fun allTables(): List<Tables>
    suspend fun fullTable(tibletittle: String): Tables?

    suspend fun addNewTable(id: String, tabletittle: String, data: String): Tables?
    suspend fun editTable(id: String, tabletittle: String, data: String): Boolean
    suspend fun deleteTable(id: String, tabletittle: String, data: String): Boolean
}