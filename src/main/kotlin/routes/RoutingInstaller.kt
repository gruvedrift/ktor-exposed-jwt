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
import com.typesafe.config.Config
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Database

/**
 * Registers all route groups in the application by delegating to individual route builders.
 * Although this is not an "Application Plugin" per se (like `ContentNegotiation` or `StatusPages`),
 * it is functionally similar in the sense that it extends the Application and encapsulates
 * a modular feature.
 */

fun Application.installRoutes(
    podracingDatabase: Database,
    authDatabase: Database,
    jwtConfig: Config
) {

    val droidRepository = DroidRepository(podracingDatabase)
    val engineRepository = EngineRepository(podracingDatabase)
    val pilotRepository = PilotRepository(podracingDatabase)
    val pitCrewRepository = PitCrewRepository(podracingDatabase)
    val podracerRepository = PodracerRepository(podracingDatabase)
    val authenticationRepository = AuthRepository(authDatabase)

    val droidService = DroidService(droidRepository)
    val engineService = EngineService(engineRepository)
    val pilotService = PilotService(pilotRepository)
    val pitCrewService = PitCrewService(pitCrewRepository)
    val podracerService = PodracerService(podracerRepository)
    val registerRacerService = RacerRegistrationService(
        engineService, podracerService, pitCrewService, droidService, pilotService
    )
    val authService = AuthService(
        authRepository = authenticationRepository,
        jwtConfig = jwtConfig
    )
    routing {
        droidRoutes(droidService)
        engineRoutes(engineService)
        pilotRoutes(pilotService)
        pitcrewRoutes(pitCrewService)
        podracerRoutes(
            podracerService = podracerService,
            authConfigName = jwtConfig.getString("config-name")
        )
        racerRegistrationRoutes(
            registerRacerService = registerRacerService,
            authConfigName = jwtConfig.getString("config-name")
            )
        authRoutes(authService = authService)
    }
}