package com.gruvedrift.plugins

import com.gruvedrift.exception.EntityNotCreatedException
import com.gruvedrift.exception.EntityNotDeletedException
import com.gruvedrift.exception.EntityNotFoundException
import com.gruvedrift.exception.EntityNotUpdatedException
import com.gruvedrift.exception.InvalidIdException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.slf4j.LoggerFactory
import kotlin.reflect.jvm.internal.impl.load.java.structure.JavaClass

private val logger = LoggerFactory.getLogger("StatusPages")

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<InvalidIdException> { call, cause ->
            logger.warn(
                "${cause.message} " +
                        "(method: ${call.request.httpMethod.value}, uri: ${call.request.uri})"
            )
            call.respond(HttpStatusCode.BadRequest, cause.message ?: "Invalid id!")
        }
        exception<EntityNotFoundException> { call, cause ->
            logger.error(
                "${cause.message}" +
                        "(method: ${call.request.httpMethod.value}, uri: ${call.request.uri})"
            )
            call.respond(HttpStatusCode.NotFound, cause.message ?: "Could not find entity!")
        }
        exception<EntityNotCreatedException> { call, cause ->
            logger.error(
                "${cause.message}" +
                        "(method: ${call.request.httpMethod.value}, uri: ${call.request.uri})"
            )
            call.respond(HttpStatusCode.InternalServerError, cause.message ?: "Entity creation failed!")
        }
        exception<EntityNotUpdatedException> { call, cause ->
            logger.error(
                "${cause.message}" +
                        "(method: ${call.request.httpMethod.value}, uri: ${call.request.uri})"
            )
        }
        exception<EntityNotDeletedException> { call, cause ->
            logger.error(
                "${cause.message}" +
                        "(method: ${call.request.httpMethod.value}, uri: ${call.request.uri})"
            )

        }
        // TODO pull out in helperfunction, too much duplicaet

    }
}


