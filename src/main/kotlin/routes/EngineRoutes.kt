package com.gruvedrift.routes

import com.gruvedrift.domain.dto.request.CreateEngineRequest
import com.gruvedrift.domain.dto.request.UpdateDroidRequest
import com.gruvedrift.domain.dto.request.UpdateEngineEffectRequest
import com.gruvedrift.exception.InvalidIdException
import com.gruvedrift.repository.EngineRepository
import com.gruvedrift.service.EngineService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.engineRoutes(
    engineRepository: EngineRepository,
    engineService: EngineService
) {
    route("/api/engine") {
        get("{id}")  {
            val id = call.parameters["id"]?.toIntOrNull() ?: throw InvalidIdException()
            val droid = engineService.getEngineById(id = id)
            call.respond(HttpStatusCode.OK, droid)
        }

        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing or invalid id!")
            val engine = engineRepository.getEngineById(id = id) ?: return@get call.respond(
                HttpStatusCode.NotFound,
                "Could not find engine with id $id"
            )
            call.respond(engine)
        }


        post("/create") {
            val request = call.receive<CreateEngineRequest>()
            val newEngineId = engineRepository.createEngine(createEngineRequest = request)
            if (newEngineId <= 0) {
                return@post call.respond(
                    HttpStatusCode.InternalServerError,
                    "Could not create engine for request: $request"
                )
            }
            call.respond(HttpStatusCode.Created, "Successfully created engine with id $newEngineId")
        }


        put("/update-effect"){
           val request = call.receive<UpdateEngineEffectRequest>()
            if(engineRepository.updateEngineEffect(updateEngineRequest =  request) <= 0 ) {
               return@put call.respond(HttpStatusCode.InternalServerError, "Could not update engine with id ${request.id}, to effect ${request.effectOutput}")
            }
            call.respond(HttpStatusCode.OK, "Updated engine with id: ${request.id} to output ${request.effectOutput} Megawatts!")
        }

        delete("/delete/{id}"){
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@delete call.respond(HttpStatusCode.BadRequest,"Missing or invalid id!")
            if(engineRepository.deleteEngine(id = id) <= 0) {
                return@delete call.respond(HttpStatusCode.InternalServerError, "Could not delete engine with id $id")
            }
            call.respond(HttpStatusCode.OK, "Successfully deleted engine with id: $id")
        }

    }
}



