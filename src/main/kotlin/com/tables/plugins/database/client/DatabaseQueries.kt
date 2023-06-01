package com.tables.plugins.database.client

import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class DatabaseQueries : IDatabaseQueries {
    private fun resultRowToTest(row: ResultRow) = Test(
        email = row[BaseForUsers.email],
        username = row[BaseForUsers.username],
        password = row[BaseForUsers.password],
    )

    override suspend fun allUsers(): List<Test> = DatabaseFactory.dbQuery {
        BaseForUsers.selectAll().map(::resultRowToTest)
    }

    /*override suspend fun findById(id: Int): Test? = DatabaseFactory.dbQuery {
        Tests
            .select { Tests. eq id }
            .map(::resultRowToTest)
            .singleOrNull()
    }*/

    override suspend fun findByName(username: String): Test? = DatabaseFactory.dbQuery {
        BaseForUsers
            .select { BaseForUsers.username eq username }
            .map(::resultRowToTest)
            .singleOrNull()
    }

    override suspend fun addNewUser(email: String, username: String, password: String): Test? = DatabaseFactory.dbQuery {
        val insertStatement = BaseForUsers.insert {
            it[BaseForUsers.email] = email
            it[BaseForUsers.username] = username
            it[BaseForUsers.password] = password
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToTest)
    }

    override suspend fun editUser(email : String, username: String, password: String): Boolean = DatabaseFactory.dbQuery {
        BaseForUsers.update({ BaseForUsers.username eq username }) {
            it[BaseForUsers.username] = username
            it[BaseForUsers.password] = password
        } > 0
    }

    override suspend fun deleteUser(email: String): Boolean = DatabaseFactory.dbQuery {
        BaseForUsers.deleteWhere { BaseForUsers.email eq email } > 0
    }
}

val my_queries: IDatabaseQueries = DatabaseQueries().apply {
    runBlocking {
        if(allUsers().isEmpty()) {
            addNewUser("email@email", "testuser", "123")
        }
    }
}
