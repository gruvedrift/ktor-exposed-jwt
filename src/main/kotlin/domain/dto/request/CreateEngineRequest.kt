package com.gruvedrift.domain.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateEngineRequest(
    val manufacturer: String,
    val effectOutput: Int,
    val model: String,
)

@Serializable
data class UpdateEngineEffectRequest(
    val id: Int,
    val effectOutput: Int,
)
