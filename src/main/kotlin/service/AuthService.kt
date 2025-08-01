package com.gruvedrift.service

import at.favre.lib.crypto.bcrypt.BCrypt
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.gruvedrift.repository.AuthRepository
import com.gruvedrift.repository.Role
import com.typesafe.config.Config
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import java.util.Date
import kotlin.text.toCharArray

class AuthService(
    private val authRepository: AuthRepository,
    private val jwtConfig: Config
) {
    fun authenticate(
        username: String,
        password: String,
    ): String? {
        // Verify that user exists
        val user = authRepository.getUserByUsername(username = username) ?: return null

        // Verify password is correct
        if (!verifyPassword(password = password, hashedPassword = user.hashedPassword)) return null

        // Get role for user
        val role = authRepository.getRoleByUserId(userId = user.id)?.name ?: return null

        return JWT.create()
            .withAudience(jwtConfig.getString("audience"))
            .withIssuer(jwtConfig.getString("issuer"))
            .withClaim("username", username)
            .withClaim("role", role)
            .withExpiresAt(Date(System.currentTimeMillis() + 600_000)) // 10 minutes till expire
            .sign(Algorithm.HMAC256(jwtConfig.getString("secret")))
    }

    private fun verifyPassword(password: String, hashedPassword: String): Boolean =
        BCrypt.verifyer().verify(password.toCharArray(), hashedPassword).verified

}

private fun ApplicationCall.hasRole(requiredRole: Role): Boolean {
    val principal = principal<JWTPrincipal>() ?: return false
    val role = principal.payload.getClaim("role").asString() ?: return false
    return Role.valueOf(role) == requiredRole
}

/**
 * With Application call receiver, we can omit the 'call' and rather
 * access the properties and functions of ApplicationCall directly. Neato!
 */
suspend fun ApplicationCall.requireRole(
    role: Role,
    block: suspend ApplicationCall.() -> Unit
) {
    if (!hasRole(role)) {
        respond(HttpStatusCode.Forbidden, "Missing required role: $role")
        return
    }
    block()
}