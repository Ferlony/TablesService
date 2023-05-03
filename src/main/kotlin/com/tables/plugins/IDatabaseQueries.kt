package com.tables.plugins

interface IDatabaseQueries {
    suspend fun allTests(): List<Test>
    suspend fun test(id: Int): Test?
    suspend fun addNewTest(title: String, body: String): Test?
    suspend fun editTest(id: Int, title: String, body: String): Boolean
    suspend fun deleteTest(id: Int): Boolean
}
