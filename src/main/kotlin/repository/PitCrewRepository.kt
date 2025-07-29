package com.gruvedrift.repository

import com.gruvedrift.domain.dto.request.CreatePitCrewRequest
import com.gruvedrift.domain.dto.request.UpdatePitCrewRequest
import com.gruvedrift.domain.model.PitCrew
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class PitCrewRepository(
    private val podracingDb: Database
){

    fun getPitCrewById(id: Int): PitCrew? = transaction(podracingDb) {
        PitCrewTable
            .selectAll()
            .where(PitCrewTable.id eq id)
            .map { it.toPitCrew() }
            .singleOrNull()
    }

    fun createPitcrew(createPitCrewRequest: CreatePitCrewRequest): Int = transaction(podracingDb) {
        PitCrewTable.insert { statement ->
            statement[crewName] = createPitCrewRequest.crewName
        } get PitCrewTable.id
    }

    fun updatePitCrew(updatePitCrewRequest: UpdatePitCrewRequest): Int = transaction(podracingDb) {
        PitCrewTable.update(
            { PitCrewTable.id eq updatePitCrewRequest.id }
        ) { statement -> statement[crewName] = updatePitCrewRequest.crewName }
    }

    fun deletePitcrew(id: Int): Int = transaction(podracingDb) {
        PitCrewTable.deleteWhere {
            PitCrewTable.id eq id
        }
    }
}

private fun ResultRow.toPitCrew(): PitCrew = PitCrew(
    id = this[PitCrewTable.id],
    crewName = this[PitCrewTable.crewName]
)

object PitCrewTable : Table("pit_crew") {
    val id = integer("id").autoIncrement()
    val crewName = varchar("crew_name", 512)
    override val primaryKey = PrimaryKey(id)
}