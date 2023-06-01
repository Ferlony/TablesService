package com.tables.plugins.database.table


interface IDatabaseTable {
    suspend fun allTables(): List<Tables>
    suspend fun fullTable(id: String): Tables?
    suspend fun addNewTable(id: String, topic: String, description: String, userstatus: String, checked: String, tabletittle: String): Tables?
    suspend fun editTable(id: String, topic: String, description: String, userstatus: String, checked: String, tabletittle: String): Boolean
    suspend fun deleteTable(id: String, topic: String): Boolean
}