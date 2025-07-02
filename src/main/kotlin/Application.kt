package com.gruvedrift

import com.gruvedrift.config.DatabaseConfig
import com.gruvedrift.plugins.configureStatusPages
import com.gruvedrift.routes.droidRoutes
import com.gruvedrift.routes.installRoutes
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(ContentNegotiation) {
        json(Json {
                prettyPrint = true
                isLenient = true
            }
        )
    }
    installRoutes()
    DatabaseConfig.init()
    configureStatusPages() // global error handler and response plugin
}