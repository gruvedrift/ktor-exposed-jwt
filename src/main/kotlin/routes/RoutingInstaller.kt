package com.gruvedrift.routes

import com.gruvedrift.repository.DroidRepository
import com.gruvedrift.repository.EngineRepository
import com.gruvedrift.repository.PilotRepository
import com.gruvedrift.repository.PitCrewRepository
import com.gruvedrift.repository.PodracerRepository
import io.ktor.server.application.*
import io.ktor.server.routing.*

private val droidRepository = DroidRepository()
private val engineRepository = EngineRepository()
private val pilotRepository = PilotRepository()
private val pitCrewRepository = PitCrewRepository()
private val podracerRepository = PodracerRepository()


fun Application.installRoutes() {
    routing {
        droidRoutes(droidRepository)
        testRoutes(
           droidRepository, engineRepository, pilotRepository, pitCrewRepository, podracerRepository
        )
    }
}
