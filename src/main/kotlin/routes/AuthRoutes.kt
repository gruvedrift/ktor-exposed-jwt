package com.gruvedrift.routes

import com.gruvedrift.domain.dto.request.UserAuthRequest
import com.gruvedrift.service.AuthService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoutes(
    authService: AuthService,
) {
    route("api/auth") {
        post("/get-token") {
            val user = call.receive<UserAuthRequest>()
            val token = authService.authenticate(user.username, user.password)
                ?: return@post call.respond(HttpStatusCode.Unauthorized, "Invalid credentials!")
            call.respond(token)
        }
    }
}