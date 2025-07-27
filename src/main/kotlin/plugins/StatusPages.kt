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

/**
 * Centralized error handling using Ktor’s `StatusPages` plugin.
 *
 * - Maps specific custom exceptions to HTTP status codes.
 * - Ensures that all domain errors return consistent response format & logs.
 *
 * This approach avoids cluttering controller logic with `try-catch` blocks
 * and separates infrastructure concerns (error handling/logging) from business logic.
 */
fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<InvalidIdException> { call, cause ->
            logAndRespond(call = call, cause = cause, loglevel = Loglevel.WARNING, httpStatusCode = HttpStatusCode.BadRequest)
        }
        exception<EntityNotFoundException> { call, cause ->
            logAndRespond(call = call, cause = cause, Loglevel.ERROR, httpStatusCode = HttpStatusCode.NotFound)
        }
        exception<EntityNotCreatedException> { call, cause ->
            logAndRespond(call = call, cause = cause, Loglevel.ERROR, httpStatusCode = HttpStatusCode.InternalServerError)
        }
        exception<EntityNotUpdatedException> { call, cause ->
            logAndRespond(call = call, cause = cause, Loglevel.ERROR, httpStatusCode = HttpStatusCode.InternalServerError)
        }
        exception<EntityNotDeletedException> { call, cause ->
            logAndRespond(call = call, cause = cause, Loglevel.ERROR, httpStatusCode = HttpStatusCode.InternalServerError)
        }

    }
}

private val logger = LoggerFactory.getLogger("StatusPages")
/**
 * Helper function that logs the exception based on severity,
 * and sends a clean HTTP response to the client.
 */
private suspend fun logAndRespond(
    call: ApplicationCall,
    cause: RuntimeException,
    loglevel: Loglevel,
    httpStatusCode: HttpStatusCode,
) {
    val message = "${cause.message}" + "(method: ${call.request.httpMethod.value}, uri: ${call.request.uri})"
    when (loglevel) {
        Loglevel.INFO -> logger.info(message)
        Loglevel.WARNING -> logger.warn(message)
        Loglevel.ERROR -> logger.error(message)
    }
    call.respond(httpStatusCode, cause.message ?: httpStatusCode.description )
}

enum class Loglevel { INFO, WARNING, ERROR }

