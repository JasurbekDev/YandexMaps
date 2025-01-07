package com.idyllic.core_imp.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.idyllic.core_api.model.RefreshTokenDto

@Keep
data class RefreshToken(
    @SerializedName("token"      ) val token     : String? = null,
    @SerializedName("expires_in" ) val expiresIn : Long?   = null,
) {
    constructor(dto: RefreshTokenDto) : this(
        dto.token, dto.expiresIn
    )

    fun toDto() = RefreshTokenDto(
        token, expiresIn
    )
}
