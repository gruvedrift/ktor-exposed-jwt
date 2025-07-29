package com.gruvedrift.routes

import com.gruvedrift.exception.InvalidIdException
import com.gruvedrift.service.AuthService
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoutes(
    authService: AuthService
){
    route("api/auth") {
        get("/{id}"){
            val userId = call.parameters["id"]?.toIntOrNull() ?: throw InvalidIdException()
            val user = authService.getById(id = userId)
            call.respond(HttpStatusCode.OK, user)
        }
    }
}