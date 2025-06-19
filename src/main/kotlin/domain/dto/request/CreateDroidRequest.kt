package com.gruvedrift.domain.dto.request

data class CreateDroidRequest(
    val pitCrewId: Int,
    val manufacturer: String,
    val price: Int,
)

data class UpdateDroidRequest(
    val droidId: Int,
    val newPitCrewId: Int,
)
