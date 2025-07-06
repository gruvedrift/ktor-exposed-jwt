package com.gruvedrift.repository

import com.gruvedrift.domain.dto.request.CreatePilotRequest
import com.gruvedrift.domain.dto.request.UpdatePilotPodracerRequest
import com.gruvedrift.domain.model.Pilot
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class PilotRepository {

    fun getPilotById(id: Int): Pilot? = transaction {
        PilotTable
            .selectAll()
            .where { PilotTable.id eq id }
            .map { it.toPilot() }
            .singleOrNull()
    }

    fun createPilot(createPilotRequest: CreatePilotRequest): Int = transaction {
        PilotTable.insert { stm ->
            stm[podracerId] = createPilotRequest.podracerId
            stm[pitCrewId] = createPilotRequest.pitCrewId
            stm[name] = createPilotRequest.name
            stm[species] = createPilotRequest.species
            stm[homePlanet] = createPilotRequest.homePlanet
        } get PilotTable.id
    }

    fun updatePilotPodracer(updatePilotPodracerRequest: UpdatePilotPodracerRequest): Int = transaction {
        PilotTable.update(
            { PilotTable.id eq updatePilotPodracerRequest.pilotId }
        ) { stm -> stm[podracerId] = updatePilotPodracerRequest.podracerId }
    }


    fun deletePilotById(id: Int): Int = transaction {
        PilotTable.deleteWhere {
            PilotTable.id eq id
        }
    }
}

object PilotTable : Table("pilot") {
    val id = integer("id").autoIncrement()
    val podracerId = integer("podracer_id").references(PodracerTable.id)
    val pitCrewId = integer("pit_crew_id").references(PitCrewTable.id)
    val name = varchar("name", 512)
    val species = varchar("species", 512)
    val homePlanet = varchar("home_planet", 512)
}

/**
 * "this" is a ResultRow. It stores data as a map of key - value pairs.
 * mapof(
 *  PilotTable. Id = 1
 *  PilotTable.podracerId = 6
 *  ...
 *  )
 *  when we say: pitCrewid = this[PilotTable.pitCrewId] we are really just doing syntactic sugar for :
 *  resultRow.get(PilotTable.pitCrewId)
 **/
private fun ResultRow.toPilot(): Pilot = Pilot(
    id = this[PilotTable.id],
    podracerId = this[PilotTable.podracerId],
    pitCrewId = this[PilotTable.pitCrewId],
    name = this[PilotTable.name],
    species = this[PilotTable.species],
    homePlanet = this[PilotTable.homePlanet]
)