package com.gruvedrift.domain.dto.request

data class CreateEngineRequest(
    val manufacturer: String,
    val effectOutput: Int,
    val model: String,
)

data class UpdateEngineEffectRequest(
    val id: Int,
    val effectOutput: Int,
)
