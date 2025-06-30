package com.gruvedrift.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Podracer(
    val id: Int,
    val engineId: Int,
    val model: String,
    val manufacturer: String,
    val weight: Int,
    val maxSpeed: Int,
)
