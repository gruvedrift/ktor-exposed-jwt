package com.gruvedrift.routes

import com.gruvedrift.domain.dto.request.CreatePodracerRequest
import com.gruvedrift.repository.PodracerRepository
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.podracerRoutes(
    podracerRepository: PodracerRepository
) {

    route("/api/podracer") {

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing or invalid id!")
            val podracer = podracerRepository.getById(id = id) ?: return@get call.respond(
                HttpStatusCode.NotFound,
                "Could not find podracer with id: $id"
            )
            call.respond(HttpStatusCode.OK, podracer)
        }

        get("/engine-model/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing or invalid id!")
            val engineManufacturer = podracerRepository.getEngineModel(podracerId = id)
                ?: return@get call.respond(
                    HttpStatusCode.NotFound,
                    "Could not find engine manufacturer for podracer with id $id "
                )
            call.respond(engineManufacturer)
        }

        post("/create") {
            val request = call.receive<CreatePodracerRequest>()
            val createdPodracerId = podracerRepository.createPodracer(createPodracerRequest = request)
            if (createdPodracerId <= 0) {
                return@post call.respond(HttpStatusCode.BadRequest, "Could not create podracer for request $request")
            }
            call.respond(HttpStatusCode.OK, "Successfully created podracer with id: $createdPodracerId")
        }

        get("analytics/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                "Missing or invalid id!"
            )
            val analytics = podracerRepository.getAnalyticsCoreData(id = id)
            println("anal: $analytics")
                if ( analytics == null ) {
                    return@get call.respond(
                        HttpStatusCode.InternalServerError,
                        "Could not retrieve analytics data for podracer with id $id"
                    )
                }
            call.respond(HttpStatusCode.OK, analytics)
        }

        delete("/delete/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respond(
                HttpStatusCode.BadRequest,
                "Missing or invalid id!"
            )
            if (podracerRepository.deletePodracer(id = id) <= 0) {
                return@delete call.respond(HttpStatusCode.InternalServerError, "Could not delete podracer with id: $id")
            }
            call.respond(HttpStatusCode.OK, "Successfully deleted podracer with id: $id")
        }
    }
}