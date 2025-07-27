package com.gruvedrift.repository

import com.gruvedrift.domain.dto.request.CreateDroidRequest
import com.gruvedrift.domain.dto.request.UpdateDroidRequest
import com.gruvedrift.domain.model.Droid
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.sql.ResultSet

class DroidRepository {

    fun getDroidById(id: Int): Droid? = transaction {
        DroidTable
            .selectAll()
            .where { DroidTable.id eq id }
            .map { it.toDroid() }
            .singleOrNull()
    }


    fun createDroid(createDroidRequest: CreateDroidRequest) : Int = transaction {
        DroidTable.insert { stm ->
            stm[pitCrewId] = createDroidRequest.pitCrewId
            stm[manufacturer] = createDroidRequest.manufacturer
            stm[price] = createDroidRequest.price
        } get DroidTable.id
    }


    fun getAllByPilotId(pilotId: Int): List<Droid> = transaction {
        DroidTable
            .selectAll()
            .where { DroidTable.pitCrewId eq pilotId }
            .map { it.toDroid() }
    }

    /**
     *  update takes in two lambdas:
     *  First -> where clause  ( predicate )
     *  Second -> update body
     */
    fun updateCrewId(updateDroidRequest: UpdateDroidRequest): Int = transaction {
        DroidTable.update(
            { DroidTable.id eq updateDroidRequest.droidId }
        ) { row ->
            row[pitCrewId] = updateDroidRequest.newPitCrewId
        }
    }

    // Only takes in one lambda -> where clause ( predicate ) so no need to wrap with ( )
    fun deleteDroid(id: Int): Int = transaction {
        DroidTable.deleteWhere {
            DroidTable.id eq id
        }
    }

    fun getAllByPilotIdRAW(pilotId: Int): List<Droid> = transaction {
        exec("SELECT * FROM droid WHERE pit_crew_id = $pilotId") { resultSet ->
            val droids = mutableListOf<Droid>()
            while (resultSet.next()) {
                droids.add(resultSet.toDroid())
            }
            droids
        } ?: emptyList()
    }
}

object DroidTable : Table("droid") {
    val id = integer("id").autoIncrement()
    val pitCrewId = integer("pit_crew_id").references(PitCrewTable.id)
    val manufacturer = varchar("manufacturer", length = 512)
    val price = integer("price")

    override val primaryKey = PrimaryKey(id)
}

fun ResultSet.toDroid(): Droid = Droid(
    id = this.getInt("id"),
    pitCrewId = this.getInt("pit_crew_id"),
    manufacturer = this.getString("manufacturer"),
    price = this.getInt("price")
)

private fun ResultRow.toDroid(): Droid = Droid(
    id = this[DroidTable.id],
    pitCrewId = this[DroidTable.pitCrewId],
    manufacturer = this[DroidTable.manufacturer],
    price = this[DroidTable.price]
)
