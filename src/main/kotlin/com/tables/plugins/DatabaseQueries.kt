package com.tables.plugins

import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class DatabaseQueries : IDatabaseQueries {
    private fun resultRowToTest(row: ResultRow) = Test(
        email = row[UsersDB.email],
        username = row[UsersDB.username],
        password = row[UsersDB.password],
        role = row[UsersDB.role]
    )

    override suspend fun allUsers(): List<Test> = DatabaseFactory.dbQuery {
        UsersDB.selectAll().map(::resultRowToTest)
    }

    /*override suspend fun findById(id: Int): Test? = DatabaseFactory.dbQuery {
        Tests
            .select { Tests. eq id }
            .map(::resultRowToTest)
            .singleOrNull()
    }*/

    override suspend fun findByName(username: String): Test? = DatabaseFactory.dbQuery {
        UsersDB
            .select { UsersDB.username eq username }
            .map(::resultRowToTest)
            .singleOrNull()
    }

    override suspend fun addNewUser(email: String, username: String, password: String, role: String): Test? = DatabaseFactory.dbQuery {
        val insertStatement = UsersDB.insert {
            it[UsersDB.email] = email
            it[UsersDB.username] = username
            it[UsersDB.password] = password
            it[UsersDB.role] = role
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToTest)
    }

    override suspend fun editUser(email : String, username: String, password: String): Boolean = DatabaseFactory.dbQuery {
        UsersDB.update({ UsersDB.username eq username }) {
            it[UsersDB.username] = username
            it[UsersDB.password] = password
        } > 0
    }

    override suspend fun deleteUser(email: String): Boolean = DatabaseFactory.dbQuery {
        UsersDB.deleteWhere { UsersDB.email eq email } > 0
    }
}

val my_queries: IDatabaseQueries = DatabaseQueries().apply {
    runBlocking {
        if(allUsers().isEmpty()) {
            addNewUser("admin@gmail.com", "root", "superadmin", "admin")
        }
    }
}
