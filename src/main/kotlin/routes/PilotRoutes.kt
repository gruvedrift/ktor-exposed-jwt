package com.gruvedrift.routes

import com.gruvedrift.domain.dto.request.CreatePilotRequest
import com.gruvedrift.domain.dto.request.UpdatePilotPodracerRequest
import com.gruvedrift.exception.InvalidIdException
import com.gruvedrift.service.PilotService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.pilotRoutes(
    pilotService: PilotService
) {

    route("/api/pilot") {

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: throw InvalidIdException()
            val pilot = pilotService.getPilotById(id = id)
            call.respond(HttpStatusCode.OK, pilot)
        }

        post("/create") {
            val request = call.receive<CreatePilotRequest>()
            val createdPilotId = pilotService.createPilot(createPilotRequest = request)
            call.respond(HttpStatusCode.Created, "Successfully created pilot with id: $createdPilotId")
        }

        put("/update") {
            val request = call.receive<UpdatePilotPodracerRequest>()
            pilotService.updatePilot(updatePilotPodracerRequest = request)
            call.respond(
                HttpStatusCode.OK,
                "Pilot with id ${request.pilotId} successfully registered to podracer with id ${request.podracerId}"
            )
        }
        delete("/delete/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: throw InvalidIdException()
            pilotService.deletePilot(id = id)
            call.respond(HttpStatusCode.OK, "Successfully deleted pilot with id: $id")
        }

    }
}