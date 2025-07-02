package com.gruvedrift.plugins

import com.gruvedrift.exception.EntityNotCreatedException
import com.gruvedrift.exception.EntityNotFoundException
import com.gruvedrift.exception.InvalidIdException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<InvalidIdException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, cause.message ?: "Invalid id!")
        }
        exception<EntityNotFoundException>{call, cause ->
            call.respond(HttpStatusCode.NotFound, cause.message ?: "Could not find entity!")
        }
        exception<EntityNotCreatedException>{call, cause ->
            call.respond(HttpStatusCode.InternalServerError, cause.message ?: "Entity creation failed!")
        }
    }
}