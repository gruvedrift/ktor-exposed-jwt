package com.gruvedrift.routes

import com.gruvedrift.domain.dto.request.CreatePilotRequest
import com.gruvedrift.domain.dto.request.UpdatePilotPodracerRequest
import com.gruvedrift.repository.PilotRepository
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.pilotRoutes(
    pilotRepository: PilotRepository
) {

    route("/api/pilot") {

        get("/{id}"){
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing or invalid id!")

            val pilot = pilotRepository.getPilotById(id = id) ?: call.respond(HttpStatusCode.NotFound, "Could not find pilot with id: $id")
            call.respond(HttpStatusCode.OK, pilot)
        }

        post("/create") {
            val request = call.receive<CreatePilotRequest>()
            val createdPilotId = pilotRepository.createPilot(createPilotRequest =  request)
            if(createdPilotId >= 0) {
                return@post call.respond(
                HttpStatusCode.InternalServerError,
                "Could not create pilot for request: $request"
                )
            }
            call.respond(HttpStatusCode.Created, "Successfully created pilot with id: $createdPilotId")
        }

        put("/update"){
            val request = call.receive<UpdatePilotPodracerRequest>()
            if(pilotRepository.updatePilotPodracer(updatePilotPodracerRequest = request) <= 0 ) {
                return@put call.respond(HttpStatusCode.InternalServerError, "Could not update pilot with id ${request.pilotId}!")
            }
            call.respond(HttpStatusCode.OK, "Pilot with id ${request.pilotId} successfully registered to podracer with id ${request.podracerId}")
        }

        delete("/delete/{id}/{test}"){
            val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respond(HttpStatusCode.BadRequest, "Missing or invalid id!")
            val tmp = call.parameters["test"].toString()
            println("Second parameter : $tmp" )
            if(pilotRepository.deletePilotById(id = id) <= 0 ) {
                return@delete call.respond(HttpStatusCode.InternalServerError, "Could not delete pilot with id $id")
            }
            call.respond(HttpStatusCode.OK, "Successfully deleted pilot with id: $id")
        }




    }

}