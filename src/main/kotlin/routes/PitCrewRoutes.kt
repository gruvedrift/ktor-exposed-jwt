package com.gruvedrift.routes

import com.gruvedrift.domain.dto.request.CreatePitCrewRequest
import com.gruvedrift.domain.dto.request.UpdatePitCrewRequest
import com.gruvedrift.exception.InvalidIdException
import com.gruvedrift.service.PitCrewService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.pitcrewRoutes(
    pitCrewService: PitCrewService
) {
    route("/api/pitcrew") {

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: throw InvalidIdException()
            val pitCrew = pitCrewService.getPitCrewById(id = id)
            call.respond(HttpStatusCode.OK, pitCrew)
        }

        post("/create"){
           val request = call.receive<CreatePitCrewRequest>()
            val newPitcrewId = pitCrewService.createPitCrew(createPitCrewRequest = request)
            call.respond(HttpStatusCode.Created, "Successfully created pit crew with id: $newPitcrewId")
        }
        put("/update") {
            val request = call.receive<UpdatePitCrewRequest>()
            pitCrewService.updatePitCrew(updatePitCrewRequest = request)
            call.respond(HttpStatusCode.OK,"Updated PitCrew with id ${request.id} to be named: ${request.crewName}")
        }
        delete("/delete/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: throw InvalidIdException()
            pitCrewService.deletePitCrew(id = id)
            call.respond(HttpStatusCode.OK, "Deleted pitcrew with id: $id and its references.")
        }
    }
}