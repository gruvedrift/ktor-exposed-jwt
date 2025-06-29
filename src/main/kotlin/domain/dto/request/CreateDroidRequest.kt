package com.gruvedrift.domain.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateDroidRequest(
    val pitCrewId: Int,
    val manufacturer: String,
    val price: Int,
)

@Serializable
data class UpdateDroidRequest(
    val droidId: Int,
    val newPitCrewId: Int,
)
