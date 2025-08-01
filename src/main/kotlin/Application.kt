package com.gruvedrift

import com.gruvedrift.config.initDatabase
import com.gruvedrift.plugins.configureAuthentication
import com.gruvedrift.plugins.configureStatusPages
import com.gruvedrift.routes.installRoutes
import com.typesafe.config.ConfigFactory
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json

/**
 * This is the primary entry point for configuring the Ktor application.
 * It is referenced by the server engine (Netty) when the application starts.
 *
 * Key responsibilities:
 * - Install Ktor plugins like `ContentNegotiation` for JSON support.
 * - Initialize application-wide components like database connection and routes.
 * - Inject environment-specific configuration (e.g., DEV vs TEST).
 */
fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module(environment: Environment = Environment.DEV) {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            }
        )
    }

    val config = ConfigFactory.load().getConfig(environment.configKey)
    val jwtConfig = config.getConfig("jwt")

    configureAuthentication(jwtConfig = jwtConfig)

    val podracingDb = initDatabase(
        databaseConfig = config.getConfig("podracing"),
        migrationLocation = "classpath:db/migration"
    )
    val authDb = initDatabase(
        databaseConfig = config.getConfig("auth"),
        migrationLocation = "classpath:db/migration-auth"
    )
    installRoutes(
        podracingDatabase = podracingDb.database,
        authDatabase = authDb.database,
        jwtConfig = jwtConfig
    )
    configureStatusPages()
}

enum class Environment(val configKey: String) {
    DEV("dev-environment"),
    TEST("test-environment"),
}