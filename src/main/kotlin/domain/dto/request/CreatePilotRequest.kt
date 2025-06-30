package com.gruvedrift.domain.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class CreatePilotRequest(
    val podracerId: Int,
    val pitCrewId: Int,
    val name: String,
    val species: String,
    val homePlanet: String,
)

@Serializable
data class UpdatePilotPodracerRequest(
    val pilotId: Int,
    val podracerId: Int,
)
