package com.gruvedrift.routes

import com.gruvedrift.repository.AuthRepository
import com.gruvedrift.repository.DroidRepository
import com.gruvedrift.repository.EngineRepository
import com.gruvedrift.repository.PilotRepository
import com.gruvedrift.repository.PitCrewRepository
import com.gruvedrift.repository.PodracerRepository
import com.gruvedrift.service.AuthService
import com.gruvedrift.service.DroidService
import com.gruvedrift.service.EngineService
import com.gruvedrift.service.PilotService
import com.gruvedrift.service.PitCrewService
import com.gruvedrift.service.PodracerService
import com.gruvedrift.service.RacerRegistrationService
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Database

/**
 * Registers all route groups in the application by delegating to individual route builders.
 * Although this is not an "Application Plugin" per se (like `ContentNegotiation` or `StatusPages`),
 * it is functionally similar in the sense that it extends the Application and encapsulates
 * a modular feature.
 */

fun Application.installRoutes(podracingDb: Database, authDatabase: Database) {

    val droidRepository = DroidRepository(podracingDb)
    val engineRepository = EngineRepository(podracingDb)
    val pilotRepository = PilotRepository(podracingDb)
    val pitCrewRepository = PitCrewRepository(podracingDb)
    val podracerRepository = PodracerRepository(podracingDb)
    val authenticationRepository = AuthRepository(authDatabase)

    val droidService = DroidService(droidRepository)
    val engineService = EngineService(engineRepository)
    val pilotService = PilotService(pilotRepository)
    val pitCrewService = PitCrewService(pitCrewRepository)
    val podracerService = PodracerService(podracerRepository)
    val registerRacerService = RacerRegistrationService(
        engineService, podracerService, pitCrewService, droidService, pilotService
    )
    val authService = AuthService(authenticationRepository)
    routing {
        droidRoutes(droidService)
        engineRoutes(engineService)
        pilotRoutes(pilotService)
        pitcrewRoutes(pitCrewService)
        podracerRoutes(podracerService)
        racerRegistrationRoutes(registerRacerService)
        authRoutes(authService)
    }
}