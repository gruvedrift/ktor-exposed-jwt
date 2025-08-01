package com.gruvedrift.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.typesafe.config.Config
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

/**
 * Configures JWT-based authentication for the application.
 *
 * Checks the structural validity of the JWT, such as
 *  - being syntactically correct and untampered.
 *  - produced with the correct secret / audience / issuer
 *  - contains expected fields i.e. "username" and "role".
 */
fun Application.configureAuthentication(
    jwtConfig: Config
) {
    install(Authentication) {
        jwt(jwtConfig.getString("config-name")) {
            realm = jwtConfig.getString("realm")
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtConfig.getString("secret")))
                    .withAudience(jwtConfig.getString("audience"))
                    .withIssuer(jwtConfig.getString("issuer"))
                    .build()
            )
            validate { credential ->
                val username = credential.payload.getClaim("username").asString()
                val role = credential.payload.getClaim("role").asString()
                if (username.isNotBlank() && role.isNotBlank()) JWTPrincipal(credential.payload) else null
            }
            challenge { _, _ -> call.respond(HttpStatusCode.Unauthorized, "Token is invalid!") }
        }
    }
}
