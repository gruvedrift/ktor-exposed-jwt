package com.gruvedrift.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Engine(
    val id: Int,
    val manufacturer: String,
    val effectOutput: Int,
    val model: String,
)
