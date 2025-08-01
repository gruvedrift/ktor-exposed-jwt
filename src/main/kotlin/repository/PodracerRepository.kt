package com.gruvedrift.repository

import com.gruvedrift.domain.dto.request.CreatePodracerRequest
import com.gruvedrift.domain.dto.response.PodracerAnalyticsCoreData
import com.gruvedrift.domain.model.Podracer
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.innerJoin
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class PodracerRepository(
    private val podracingDb: Database
){

    /**
     * Each function runs in its own database transaction context.
     * `transaction {}` is provided by Exposed, and guarantees safe and consistent access.
     */
    fun getById(id: Int): Podracer? = transaction(podracingDb) {
        PodracerTable
            .selectAll()
            .where(PodracerTable.id eq id)
            .map { it.toPodracer() }
            .singleOrNull()
    }

    fun getEngineModel(podracerId: Int): String? = transaction(podracingDb) {
        (PodracerTable innerJoin EngineTable)
            .selectAll()
            .where(PodracerTable.id eq podracerId)
            .map { it[EngineTable.model] }
            .singleOrNull()
    }

    fun createPodracer(createPodracerRequest: CreatePodracerRequest): Int = transaction(podracingDb) {
        PodracerTable.insert { stm ->
            stm[engineId] = createPodracerRequest.engineId
            stm[model] = createPodracerRequest.model
            stm[manufacturer] = createPodracerRequest.manufacturer
            stm[weight] = createPodracerRequest.weight
            stm[maxSpeed] = createPodracerRequest.maxSpeed
        } get PodracerTable.id
    }

    fun deletePodracer(id: Int): Int = transaction(podracingDb) {
        PodracerTable.deleteWhere {
            PodracerTable.id eq id
        }
    }

    fun getAnalyticsCoreData(id: Int): PodracerAnalyticsCoreData? = transaction(podracingDb) {
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

// Singleton object for mapping between domain objects to database objects. It is a base class for any DB table.
object PodracerTable : Table("podracer") {
    val id = integer("id").autoIncrement()
    val engineId = integer("engine_id").references(EngineTable.id)
    val model = varchar("model", 512)
    val manufacturer = varchar("manufacturer", 512)
    val weight = integer("weight")
    val maxSpeed = integer("max_speed")

    override val primaryKey = PrimaryKey(id)
}

/**
 * Extension function to map a raw ResultRow (from Exposed) into a domain model (Podracer).
 * Indexing operator `this[Table.column]` is syntactic sugar for ResultRow.get(Column).
 *
 * `this` refers to a ResultRow — essentially a row returned from a SQL query.
 * Internally, a ResultRow stores values in a map-like structure, associating table columns with their values.
 *
 * Example:
 *   mapOf(
 *     PodracerTable.id to 1,
 *     PodracerTable.engineId to 4,
 *     PodracerTable.model to "Plug-F Mammoth Split-X",
 *     ...
 *   )
 *
 * So when you write: val pitCrewId = this[PilotTable.pitCrewId]
 * we are really doing: val engineId = resultRow.get(PodracerTable.engineId)
 *
 * The indexing operator (`[]`) is just syntactic sugar for `.get()`.
 */
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

/**
 * Function reference syntax (e.g., .map(::toCoreData)) is an alternative to using lambdas.
 * Improves readability and reuse when mapping from ResultRow to domain types.
 */
private fun toCoreData(row: ResultRow): PodracerAnalyticsCoreData = PodracerAnalyticsCoreData(
    weight = row[PodracerTable.weight],
    maxSpeed = row[PodracerTable.maxSpeed],
    engineEffectOutput = row[EngineTable.effectOutput],
    pilotName = row[PilotTable.name]
)
