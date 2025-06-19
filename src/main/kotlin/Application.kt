package com.gruvedrift

import com.gruvedrift.config.DatabaseConfig
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    DatabaseConfig.init()
    println("HELLO")
}

