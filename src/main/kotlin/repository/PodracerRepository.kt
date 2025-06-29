package com.gruvedrift.repository

import com.gruvedrift.domain.model.Podracer
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inSubQuery
import org.jetbrains.exposed.sql.Table
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

//    fun getEngineManufacturer(podracerId: Int): String? = transaction {
//        ()
//    }




    // get engine manufacturare
    // create podracer
    // delete podracer
    // get analytics
}

object PodracerTable : Table() {
    val id = integer("id").autoIncrement()
    val engineId = integer("engine_id")
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

