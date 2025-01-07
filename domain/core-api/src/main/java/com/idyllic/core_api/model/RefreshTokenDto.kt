package com.idyllic.core_api.model

data class RefreshTokenDto(
    val token: String? = null,
    val expiresIn: Long? = null,
)
