package com.gruvedrift.routes

import com.gruvedrift.domain.dto.request.RegisterRacerRequest
import com.gruvedrift.repository.Role
import com.gruvedrift.service.RacerRegistrationService
import com.gruvedrift.service.requireRole
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.racerRegistrationRoutes(
    registerRacerService: RacerRegistrationService,
    authConfigName: String
) {
    route("/api/racers") {
        authenticate(authConfigName) {
            post("/register") {
                call.requireRole(Role.PILOT) {
                    val request = receive<RegisterRacerRequest>()
                    val registeredRacerId = registerRacerService.registerRacer(registerRacerRequest = request)
                    respond(
                        HttpStatusCode.Created,
                        "Registered racer: ${request.name} with id: $registeredRacerId"
                    )
                }
            }
        }
    }
}