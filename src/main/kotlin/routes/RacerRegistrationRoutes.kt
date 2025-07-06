package com.gruvedrift.routes

import com.gruvedrift.domain.dto.request.RegisterRacerRequest
import com.gruvedrift.service.RacerRegistrationService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.racerRegistrationRoutes(
    registerRacerService: RacerRegistrationService
) {
    route("/api/racers") {
        post("/register") {
            val request = call.receive<RegisterRacerRequest>()
            val registeredRacerId = registerRacerService.registerRacer(registerRacerRequest = request)
            call.respond(
                HttpStatusCode.Created,
                "Registered racer: ${request.name} with id: $registeredRacerId"
            )
        }
    }
}