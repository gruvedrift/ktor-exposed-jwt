package com.gruvedrift.routes

import com.gruvedrift.repository.DroidRepository
import com.gruvedrift.repository.EngineRepository
import com.gruvedrift.repository.PilotRepository
import com.gruvedrift.repository.PitCrewRepository
import com.gruvedrift.repository.PodracerRepository
import com.gruvedrift.service.DroidService
import com.gruvedrift.service.EngineService
import com.gruvedrift.service.PilotService
import com.gruvedrift.service.PitCrewService
import com.gruvedrift.service.PodracerService
import io.ktor.server.application.*
import io.ktor.server.routing.*

private val droidRepository = DroidRepository()
private val engineRepository = EngineRepository()
private val pilotRepository = PilotRepository()
private val pitCrewRepository = PitCrewRepository()
private val podracerRepository = PodracerRepository()
private val droidService = DroidService(droidRepository)
private val engineService = EngineService(engineRepository)
private val pilotService = PilotService(pilotRepository)
private val pitCrewService = PitCrewService(pitCrewRepository)
private val podracerService = PodracerService(podracerRepository)


fun Application.installRoutes() {
    routing {
        droidRoutes(droidService)
        engineRoutes(engineService)
        pilotRoutes(pilotService)
        pitcrewRoutes(pitCrewService)
        podracerRoutes(podracerService)
    }
}
