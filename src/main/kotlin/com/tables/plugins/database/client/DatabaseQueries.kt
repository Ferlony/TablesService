package com.tables.plugins.database.client

import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class DatabaseQueries : IDatabaseQueries {
    private fun resultRowToTest(row: ResultRow) = Test(
        email = row[Users.email],
        username = row[Users.username],
        password = row[Users.password],
        role = row[Users.role],
        table = row[Users.table]
    )

    override suspend fun allUsers(): List<Test> = DatabaseFactory.dbQuery {
        Users.selectAll().map(::resultRowToTest)
    }

    /*override suspend fun findById(id: Int): Test? = DatabaseFactory.dbQuery {
        Tests
            .select { Tests. eq id }
            .map(::resultRowToTest)
            .singleOrNull()
    }*/

    override suspend fun findByName(username: String): Test? = DatabaseFactory.dbQuery {
        Users
            .select { Users.username eq username }
            .map(::resultRowToTest)
            .singleOrNull()
    }

    override suspend fun findByEmail(email: String): Test? = DatabaseFactory.dbQuery {
        Users
            .select { Users.email eq email }
            .map(::resultRowToTest)
            .singleOrNull()
    }

    override suspend fun addNewUser(email: String, username: String, password: String, role: String, table: String): Test? = DatabaseFactory.dbQuery {
        val insertStatement = Users.insert {
            it[Users.email] = email
            it[Users.username] = username
            it[Users.password] = password
            it[Users.role] = role
            it[Users.table] = table
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToTest)
    }

    override suspend fun editUser(username: String, role: String): Boolean = DatabaseFactory.dbQuery {
        Users.update({ Users.username eq username }) {
            it[Users.role] = role
        } > 0
    }

    override suspend fun editUserTable(username: String, newtable: String): Boolean = DatabaseFactory.dbQuery {
        Users.update({ Users.username eq username }) {
            it[table] = newtable
        } > 0
    }

    override suspend fun deleteUser(email: String): Boolean = DatabaseFactory.dbQuery {
        Users.deleteWhere { Users.email eq email } > 0
    }
}

val my_queries: IDatabaseQueries = DatabaseQueries().apply {
    runBlocking {
        if(allUsers().isEmpty()) {
            addNewUser("admin@gmail.com", "admin", "admin", "admin", "all") // дает все таблицы
            addNewUser("moderator@gmail.com", "moderator", "moderator", "mod", "all")
        }
    }
}
