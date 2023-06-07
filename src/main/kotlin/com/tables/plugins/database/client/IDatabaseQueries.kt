package com.tables.plugins.database.client

interface IDatabaseQueries {
    suspend fun allUsers(): List<Test>
    //suspend fun findById(id: Int): Test?
    suspend fun findByName(name: String): Test?
    suspend fun findByEmail(email: String): Test?
    suspend fun addNewUser(email: String, title: String, body: String, role: String, table: String): Test?

    suspend fun editUser(username: String, role: String): Boolean
    suspend fun editUserTable(username: String, newtable: String): Boolean
    suspend fun deleteUser(email: String): Boolean

}
