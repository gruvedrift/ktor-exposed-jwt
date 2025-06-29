package com.gruvedrift.routes

import com.gruvedrift.domain.dto.request.CreateDroidRequest
import com.gruvedrift.domain.dto.request.UpdateDroidRequest
import com.gruvedrift.domain.model.Droid
import com.gruvedrift.repository.DroidRepository
import io.ktor.client.plugins.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.droidRoutes(
    droidRepository: DroidRepository
) {
    route("/api/droid") { // equivalent of a + @RequestMapping

        get("/{id}") { // equivalent of a  @GetMapping
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing or invalid droid id!")
            val droid = droidRepository.getDroidById(id) ?: return@get call.respond(
                HttpStatusCode.NotFound,
                "Could not find Droid with id $id"
            )
            call.respond(droid)
        }

        // TODO response for empty list
        get("/droid-crew/{pilotId}") {
            val pilotId = call.parameters["pilotId"]?.toIntOrNull()
                ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing or invalid pilot id!")
            val droidCrew = droidRepository.getAllByPilotId(pilotId = pilotId)
            call.respond(droidCrew)
        }

        post("/create") {
            val request = call.receive<CreateDroidRequest>()
            val createdDroidId = droidRepository.createDroid(createDroidRequest = request)
            if (createdDroidId <= 0) {
                return@post call.respond(HttpStatusCode.InternalServerError, "Could not create droid!")
            } else call.respond("Created droid successfully with id: $createdDroidId")

        }

        // TODO better handling of updated rows?
        put("/update-crew") {
            val request = call.receive<UpdateDroidRequest>()
            if (
                droidRepository.updateCrewId(updateDroidRequest = request) <= 0
            ) {
                return@put call.respond(
                    HttpStatusCode.InternalServerError,
                    "Could not update droid with id ${request.droidId} to crew ${request.newPitCrewId}"
                )
            } else
                call.respond(
                    HttpStatusCode.OK,
                    "Updated droid with id ${request.droidId} to join crew with id ${request.newPitCrewId} "
                )
        }

        delete("/delete/{id}") {
            val id = call.pathParameters["id"]?.toIntOrNull()
                ?: return@delete call.respond(HttpStatusCode.BadRequest, "Missing or invalid droid id!")

            if (droidRepository.deleteDroid(id = id) <= 0 ) {
                return@delete call.respond(HttpStatusCode.InternalServerError, "Could not find or delete droid with id: $id")
            }
            call.respond(HttpStatusCode.OK, "Successfully deleted droid with id: $id")
        }

    }
}