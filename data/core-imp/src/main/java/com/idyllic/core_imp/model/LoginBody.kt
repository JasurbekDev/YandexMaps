package com.idyllic.core_imp.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.idyllic.core_api.model.LoginBodyDto

@Keep
data class LoginBody(
    @SerializedName("phone_number" ) var phoneNumber : String? = null,
    @SerializedName("password"     ) var password    : String? = null
) {

    constructor(dto: LoginBodyDto) : this(
        dto.phoneNumber,
        dto.password
    )

    fun toDto() = LoginBodyDto(
        phoneNumber,
        password
    )

}