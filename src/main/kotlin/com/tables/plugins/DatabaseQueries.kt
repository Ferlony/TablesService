package com.tables.plugins

import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class DatabaseQueries : IDatabaseQueries {
    private fun resultRowToTest(row: ResultRow) = Test(
        id = row[Tests.id],
        title = row[Tests.title],
        body = row[Tests.body],
    )

    override suspend fun allTests(): List<Test> = DatabaseFactory.dbQuery {
        Tests.selectAll().map(::resultRowToTest)
    }

    override suspend fun test(id: Int): Test? = DatabaseFactory.dbQuery {
        Tests
            .select { Tests.id eq id }
            .map(::resultRowToTest)
            .singleOrNull()
    }

    override suspend fun addNewTest(title: String, body: String): Test? = DatabaseFactory.dbQuery {
        val insertStatement = Tests.insert {
            it[Tests.title] = title
            it[Tests.body] = body
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToTest)
    }

    override suspend fun editTest(id: Int, title: String, body: String): Boolean = DatabaseFactory.dbQuery {
        Tests.update({ Tests.id eq id }) {
            it[Tests.title] = title
            it[Tests.body] = body
        } > 0
    }

    override suspend fun deleteTest(id: Int): Boolean = DatabaseFactory.dbQuery {
        Tests.deleteWhere { Tests.id eq id } > 0
    }
}

val my_queries: IDatabaseQueries = DatabaseQueries().apply {
    runBlocking {
        if(allTests().isEmpty()) {
            addNewTest("Im tired", "Need to implement docker...")
        }
    }
}
