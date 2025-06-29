package com.gruvedrift.routes

import com.gruvedrift.domain.dto.request.CreateDroidRequest
import com.gruvedrift.repository.DroidRepository
import com.gruvedrift.repository.EngineRepository
import com.gruvedrift.repository.PilotRepository
import com.gruvedrift.repository.PitCrewRepository
import com.gruvedrift.repository.PodracerRepository
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.testRoutes(
   droidRepository: DroidRepository,
   engineRepository: EngineRepository,
   pilotRepository: PilotRepository,
   pitCrewRepository: PitCrewRepository,
   podracerRepository: PodracerRepository
) {
    route("/api/test") {
        post("/create") {
            val droidRequest = call.receive<CreateDroidRequest>()
            println("Got request: $droidRequest")
            var droidId = droidRepository.createDroid(createDroidRequest = droidRequest)
            if (droidId <= 0 ) {
                return@post call.respond(HttpStatusCode.BadRequest, "Could not create droid for request: $droidRequest")
            }
            call.respond("Created droid successfully with id: $droidId")
        }
    }
}