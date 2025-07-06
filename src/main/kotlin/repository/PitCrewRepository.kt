package com.gruvedrift.repository

import com.gruvedrift.domain.dto.request.CreatePitCrewRequest
import com.gruvedrift.domain.dto.request.UpdatePitCrewRequest
import com.gruvedrift.domain.model.PitCrew
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class PitCrewRepository {

    fun getPitCrewById(id: Int): PitCrew? = transaction {
        PitCrewTable
            .selectAll()
            .where(PitCrewTable.id eq id)
            .map { it.toPitCrew() }
            .singleOrNull()
    }

    fun createPitcrew(createPitCrewRequest: CreatePitCrewRequest): Int = transaction {
        PitCrewTable.insert { statement ->
            statement[crewName] = createPitCrewRequest.crewName
        }.insertedCount
        // or } get PitCrewTable.id hvis man vil returnere ID
    }

    // Returns number of rows affected
    fun updatePitCrew(updatePitCrewRequest: UpdatePitCrewRequest): Int = transaction {
        PitCrewTable.update(
            { PitCrewTable.id eq updatePitCrewRequest.id }
        ) { statement -> statement[crewName] = updatePitCrewRequest.crewName }
    }

    // deletes references ( sets null )
    fun deletePitcrew(id: Int): Int = transaction {
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