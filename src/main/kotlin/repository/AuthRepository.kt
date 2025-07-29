package com.gruvedrift.repository

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class AuthRepository(
    private val authDb: Database
) {
    fun getUserById(id: Int) : User? = transaction(authDb) {
        UserTable
            .selectAll()
            .where{ UserTable.id eq id }
            .map { it.toUser() }
            .singleOrNull()
    }
}

object UserTable: Table("users") {
    val id = integer("id").autoIncrement()
    val username = text("username")
    val password = text("password")
    override val primaryKey = PrimaryKey(id)
}

fun ResultRow.toUser() : User = User(
    id = this[UserTable.id],
    username = this[UserTable.username],
    password = this[UserTable.password],
)


@Serializable
data class User(
    val id: Int,
    val username: String,
    val password: String,
)