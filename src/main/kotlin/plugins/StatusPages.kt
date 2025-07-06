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

private val logger = LoggerFactory.getLogger("StatusPages")

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

