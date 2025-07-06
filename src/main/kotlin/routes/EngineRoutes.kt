package com.gruvedrift.routes

import com.gruvedrift.domain.dto.request.CreateEngineRequest
import com.gruvedrift.domain.dto.request.UpdateEngineEffectRequest
import com.gruvedrift.exception.InvalidIdException
import com.gruvedrift.service.EngineService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.engineRoutes(
    engineService: EngineService
) {
    route("/api/engine") {
        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: throw InvalidIdException()
            val droid = engineService.getEngineById(id = id)
            call.respond(HttpStatusCode.OK, droid)
        }

        post("/create") {
            val request = call.receive<CreateEngineRequest>()
            val newEngineId = engineService.createEngine(createEngineRequest = request)
            call.respond(HttpStatusCode.Created, "Created engine successfully with id: $newEngineId")
        }

        put("/update-effect") {
            val request = call.receive<UpdateEngineEffectRequest>()
            engineService.updateEngineEffect(updateEngineEffectRequest = request)
            call.respond(
                HttpStatusCode.OK,
                "Updated engine with id ${request.id} to output ${request.effectOutput} Megawatt!"
            )
        }

        delete("/delete/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: throw InvalidIdException()
            engineService.deleteEngine(id = id)
            call.respond(HttpStatusCode.OK, "Successfully deleted engine with id: $id")
        }

    }
}



