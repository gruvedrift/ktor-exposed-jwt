package com.gruvedrift.routes

import com.gruvedrift.domain.dto.request.CreatePodracerRequest
import com.gruvedrift.exception.InvalidIdException
import com.gruvedrift.repository.PodracerRepository
import com.gruvedrift.service.PodracerService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.podracerRoutes(
    podracerService: PodracerService
) {

    route("/api/podracer") {

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: throw InvalidIdException()
            val podracer = podracerService.getPodracerById(id = id)
            call.respond(HttpStatusCode.OK, podracer)
        }

        get("/engine-model/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: throw InvalidIdException()
            val engineModel = podracerService.getEngineModel(id = id)
            call.respond(HttpStatusCode.OK, "Engine model: $engineModel")
        }

        post("/create") {
            val request = call.receive<CreatePodracerRequest>()
            val createdPodracerId = podracerService.createPodracer(createPodracerRequest = request)
            call.respond(HttpStatusCode.Created, "Successfully created podracer with id: $createdPodracerId")
        }

        get("analytics/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: throw InvalidIdException()
            val analytics = podracerService.getAnalyticsForPodracer(id = id)
            call.respond(HttpStatusCode.OK, analytics)
        }

        delete("/delete/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: throw InvalidIdException()
            podracerService.deletePodracer(id = id)
            call.respond(HttpStatusCode.OK, "Successfully deleted podracer with id: $id")
        }
    }
}