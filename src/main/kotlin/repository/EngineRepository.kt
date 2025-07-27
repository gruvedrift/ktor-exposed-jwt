package com.gruvedrift.repository

import com.gruvedrift.domain.dto.request.CreateEngineRequest
import com.gruvedrift.domain.dto.request.UpdateEngineEffectRequest
import com.gruvedrift.domain.model.Engine
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class EngineRepository {

    fun getEngineById(id: Int): Engine? = transaction {
        EngineTable
            .selectAll()
            .where { EngineTable.id eq id }
            .map { it.toEngine() }
            .singleOrNull()
    }

    fun createEngine(createEngineRequest: CreateEngineRequest): Int = transaction {
        EngineTable.insert { stm ->
            stm[manufacturer] = createEngineRequest.manufacturer
            stm[effectOutput] = createEngineRequest.effectOutput
            stm[model] = createEngineRequest.model
        } get EngineTable.id
    }

    fun updateEngineEffect(updateEngineRequest: UpdateEngineEffectRequest): Int = transaction {
        EngineTable.update(
            { EngineTable.id eq updateEngineRequest.id }
        ) { stm ->
            stm[effectOutput] = updateEngineRequest.effectOutput
        }
    }

    // Only for reference without trailing lambda
    fun updateEngineEffectExplicit(updateEngineRequest: UpdateEngineEffectRequest): Int = transaction {
        EngineTable.update(
            where = { EngineTable.id eq updateEngineRequest.id },
            limit = null,
            body = { statement -> statement[effectOutput] = updateEngineRequest.effectOutput }
        )
    }

    // deleteWhere only takes in a single lambda, so can just write it out as it stands, no need to wrap it
    fun deleteEngine(id: Int): Int = transaction {
        EngineTable.deleteWhere {
            EngineTable.id eq id
        }
    }
}

object EngineTable : Table("engine") {
    val id = integer("id").autoIncrement()
    val manufacturer = varchar("manufacturer", length = 512)
    val effectOutput = integer("effect_output")
    val model = varchar("model", length = 512)

    override val primaryKey = PrimaryKey(id)
}

private fun ResultRow.toEngine(): Engine = Engine(
    id = this[EngineTable.id],
    manufacturer = this[EngineTable.manufacturer],
    effectOutput = this[EngineTable.effectOutput],
    model = this[EngineTable.model],
)
