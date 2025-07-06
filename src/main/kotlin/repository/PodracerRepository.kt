package com.gruvedrift.repository

import com.gruvedrift.domain.dto.request.CreatePodracerRequest
import com.gruvedrift.domain.dto.response.PodracerAnalyticsCoreData
import com.gruvedrift.domain.model.Podracer
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.innerJoin
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class PodracerRepository {

    fun getById(id: Int): Podracer? = transaction {
        PodracerTable
            .selectAll()
            .where(PodracerTable.id eq id)
            .map { it.toPodracer() }
            .singleOrNull()
    }

    fun getEngineModel(podracerId: Int): String? = transaction {
        (PodracerTable innerJoin EngineTable)
            .selectAll()
            .where(PodracerTable.id eq podracerId)
            .map { it[EngineTable.model] }
            .singleOrNull()
    }

    fun createPodracer(createPodracerRequest: CreatePodracerRequest): Int = transaction {
        PodracerTable.insert { stm ->
            stm[engineId] = createPodracerRequest.engineId
            stm[model] = createPodracerRequest.model
            stm[manufacturer] = createPodracerRequest.manufacturer
            stm[weight] = createPodracerRequest.weight
            stm[maxSpeed] = createPodracerRequest.maxSpeed
        } get PodracerTable.id
    }

    fun deletePodracer(id: Int): Int = transaction {
        PodracerTable.deleteWhere {
            PodracerTable.id eq id
        }
    }

    fun getAnalyticsCoreData(id: Int): PodracerAnalyticsCoreData? = transaction {
        (PodracerTable
            .innerJoin(EngineTable, { PodracerTable.engineId }, { EngineTable.id })
            .innerJoin(PilotTable, { PodracerTable.id }, { PilotTable.podracerId })
        )
            .selectAll()
            .where(PodracerTable.engineId eq id)
            .map { it.toPodracerAnalyticsCoreData() }
            .singleOrNull()
    }
}

object PodracerTable : Table("podracer") {
    val id = integer("id").autoIncrement()
    val engineId = integer("engine_id").references(EngineTable.id)
    val model = varchar("model", 512)
    val manufacturer = varchar("manufacturer", 512)
    val weight = integer("weight")
    val maxSpeed = integer("max_speed")

    override val primaryKey = PrimaryKey(id)
}

// When you use this[PodracerTable.id] we are using what is called an indexing operator, it is syntactic sugar for get(whatever)
private fun ResultRow.toPodracer(): Podracer = Podracer(
    id = this[PodracerTable.id],
    engineId = this[PodracerTable.engineId],
    model = this[PodracerTable.model],
    manufacturer = this[PodracerTable.manufacturer],
    weight = this[PodracerTable.weight],
    maxSpeed = this[PodracerTable.maxSpeed]
)

private fun ResultRow.toPodracerAnalyticsCoreData(): PodracerAnalyticsCoreData = PodracerAnalyticsCoreData(
    weight = this[PodracerTable.weight],
    maxSpeed = this[PodracerTable.maxSpeed],
    engineEffectOutput = this[EngineTable.effectOutput],
    pilotName = this[PilotTable.name]
)

// It is also possible to do take the ResultRow as an argument.
// Can then call .map(::toCoreData). This is called a function reference.
private fun toCoreData(row: ResultRow): PodracerAnalyticsCoreData = PodracerAnalyticsCoreData(
    weight = row[PodracerTable.weight],
    maxSpeed = row[PodracerTable.maxSpeed],
    engineEffectOutput = row[EngineTable.effectOutput],
    pilotName = row[PilotTable.name]
)
