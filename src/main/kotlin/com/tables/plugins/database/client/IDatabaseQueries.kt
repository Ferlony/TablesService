package com.tables.plugins.database.client

import com.tables.plugins.database.client.Test

interface IDatabaseQueries {
    suspend fun allUsers(): List<Test>
    //suspend fun findById(id: Int): Test?
    suspend fun findByName(name: String): Test?
    suspend fun addNewUser(email: String,title: String, body: String, role: String, table: String): Test?
    suspend fun editUser(email: String, title: String, body: String): Boolean
    suspend fun deleteUser(email: String): Boolean

}
