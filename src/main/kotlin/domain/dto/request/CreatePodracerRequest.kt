package com.gruvedrift.domain.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class CreatePodracerRequest(
    val engineId: Int,
    val model: String,
    val manufacturer: String,
    val weight: Int,
    val maxSpeed: Int,
)
