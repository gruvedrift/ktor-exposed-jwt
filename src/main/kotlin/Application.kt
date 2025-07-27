package com.gruvedrift

import com.gruvedrift.config.DatabaseConfig
import com.gruvedrift.plugins.configureStatusPages
import com.gruvedrift.routes.installRoutes
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json

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
    installRoutes()
    DatabaseConfig.init(environment = environment)
    configureStatusPages()
}

enum class Environment(val environmentName: String ){
    DEV("dev-environment"),
    TEST("test-environment"),
}