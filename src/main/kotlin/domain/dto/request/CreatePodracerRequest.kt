package com.gruvedrift.domain.dto.request

data class CreatePodracerRequest(
    val engineId: Int,
    val model: String,
    val manufacturer: String,
    val weight: Int,
    val maxSpeed: Int,
)
