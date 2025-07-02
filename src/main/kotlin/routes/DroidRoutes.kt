package com.gruvedrift.routes

import com.gruvedrift.domain.dto.request.CreateDroidRequest
import com.gruvedrift.domain.dto.request.UpdateDroidRequest
import com.gruvedrift.exception.InvalidIdException
import com.gruvedrift.service.DroidService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.droidRoutes(
    droidService: DroidService
) {
    route("/api/droid") {

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: throw InvalidIdException()
            val droid = droidService.getDroidById(id = id)
            call.respond(HttpStatusCode.OK, droid)
        }

        get("/droid-crew/{pilotId}") {
            val pilotId = call.parameters["pilotId"]?.toIntOrNull() ?: throw InvalidIdException()
            val droidCrew = droidService.getAllByPilotId(pilotId = pilotId)
            call.respond(droidCrew)
        }

        post("/create") {
            val request = call.receive<CreateDroidRequest>()
            val createdDroidId = droidService.createDroid(createDroidRequest = request)
            call.respond(HttpStatusCode.Created, "Created droid successfully with id: $createdDroidId")
        }

        put("/update-crew") {
            val request = call.receive<UpdateDroidRequest>()
            droidService.updateDroidCrew(updateDroidRequest = request)
            call.respond(
                HttpStatusCode.OK,
                "Droid ${request.droidId} moved to crew ${request.newPitCrewId}"
            )
        }

        delete("/delete/{id}") {
            val id = call.pathParameters["id"]?.toIntOrNull() ?: throw InvalidIdException()
            droidService.deleteDroid(id = id)
            call.respond(HttpStatusCode.OK, "Successfully deleted droid with id: $id")
        }

    }
}