package com.gruvedrift.repository

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.innerJoin
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class AuthRepository(
    private val authDb: Database
) {
    fun getUserByUsername(username: String) : User? = transaction(authDb) {
        UserTable
            .selectAll()
            .where{ UserTable.username eq username }
            .map { it.toUser() }
            .singleOrNull()
    }

    fun getRoleByUserId(userId: Int) : Role? = transaction(authDb) {
        ( UserTable
            .innerJoin(UserRoleTable, { id }, { UserRoleTable.userId })
            .innerJoin(RoleTable, {UserRoleTable.roleId}, { id } )
        )
            .selectAll()
            .where(UserRoleTable.userId eq userId)
            .map { Role.valueOf(it[RoleTable.name])}
            .singleOrNull()
    }

}

object RoleTable: Table("role") {
    val id = integer("id").autoIncrement()
    val name = text("name")
    override val primaryKey = PrimaryKey(id)
}

object UserTable: Table("users") {
    val id = integer("id").autoIncrement()
    val username = text("username")
    val password = text("password")
    override val primaryKey = PrimaryKey(id)
}

object UserRoleTable: Table("user_role") {
    val userId = integer("user_id")
    val roleId = integer("role_id")
    override  val primaryKey = PrimaryKey(userId, roleId)
}

private fun ResultRow.toUser() : User = User(
    id = this[UserTable.id],
    username = this[UserTable.username],
    hashedPassword = this[UserTable.password],
)

enum class Role {  ADMIN, PILOT, SPECTATOR }

data class User(
    val id: Int,
    val username: String,
    val hashedPassword: String,
)