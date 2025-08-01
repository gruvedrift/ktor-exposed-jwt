package com.gruvedrift.domain.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class UserAuthRequest(
    val username: String,
    val password: String,
)
