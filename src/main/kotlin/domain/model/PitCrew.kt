package com.gruvedrift.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PitCrew(
    val id: Int,
    val crewName: String,
)
