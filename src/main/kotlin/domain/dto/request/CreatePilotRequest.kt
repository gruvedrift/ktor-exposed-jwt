package com.gruvedrift.domain.dto.request

data class CreatePilotRequest(
    val podracerId: Int,
    val pitCrewId: Int,
    val name: String,
    val species: String,
    val homePlanet: String,
)

data class UpdatePilotPodracerRequest(
    val pilotId: Int,
    val podracerId: Int,
)
