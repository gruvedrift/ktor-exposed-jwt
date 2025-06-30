package com.gruvedrift.domain.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class CreatePitCrewRequest(
    val crewName: String,
)

@Serializable
data class UpdatePitCrewRequest(
    val id: Int,
    val crewName: String,
)
