package com.gruvedrift.domain.model

import kotlinx.serialization.Serializable

// Annotated with the Serializable type from kotlinx serialization library.
@Serializable
data class Droid(
    val id: Int,
    val pitCrewId: Int,
    val manufacturer: String,
    val price: Int,
)
