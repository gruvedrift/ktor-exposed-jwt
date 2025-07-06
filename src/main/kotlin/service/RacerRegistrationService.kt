package com.gruvedrift.service

import com.gruvedrift.domain.dto.request.CreatePilotRequest
import com.gruvedrift.domain.dto.request.RegisterRacerRequest

class RacerRegistrationService(
    private val engineService: EngineService,
    private val podracerService: PodracerService,
    private val pitCrewService: PitCrewService,
    private val droidService: DroidService,
    private val pilotService: PilotService,
) {

    fun registerRacer(registerRacerRequest: RegisterRacerRequest): Int {
        val engineId = engineService.createEngine(createEngineRequest = registerRacerRequest.engine)
        val podracerId = podracerService.createPodracer(
            createPodracerRequest = registerRacerRequest.podracer.copy(
                engineId = engineId
            )
        )
        val pitCrewId = pitCrewService.createPitCrew(createPitCrewRequest = registerRacerRequest.pitCrew)
        registerRacerRequest.droids.forEach { droid ->
            droidService.createDroid(createDroidRequest = droid.copy(pitCrewId = pitCrewId))
        }
        return pilotService.createPilot(
            createPilotRequest = CreatePilotRequest(
                podracerId = podracerId,
                pitCrewId = pitCrewId,
                name = registerRacerRequest.name,
                species = registerRacerRequest.species,
                homePlanet = registerRacerRequest.homePlanet
            )
        )
    }
}