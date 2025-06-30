package com.gruvedrift.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Pilot(
    val id: Int,
    val podracerId: Int,
    val pitCrewId: Int,
    val name: String,
    val species: String,
    val homePlanet: String,
)
