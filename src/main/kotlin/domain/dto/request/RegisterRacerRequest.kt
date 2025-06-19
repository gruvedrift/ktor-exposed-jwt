package com.gruvedrift.domain.dto.request

data class RegisterRacerRequest(
    val name: String,
    val species: String,
    val homePlanet: String,
    val engine: CreateEngineRequest,
    val podracer: CreatePodracerRequest,
    val pitCrew: CreatePitCrewRequest,
    val droids: List<CreateDroidRequest>,
)
