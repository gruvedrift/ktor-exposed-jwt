package com.gruvedrift.domain.dto.request

data class CreatePitCrewRequest(
    val crewName: String,
)

data class UpdatePitCrewRequest(
    val id: Int,
    val crewName: String,
)
