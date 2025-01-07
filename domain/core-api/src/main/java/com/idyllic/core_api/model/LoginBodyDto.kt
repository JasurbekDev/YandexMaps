package com.idyllic.core_api.model

data class LoginBodyDto(
    var phoneNumber : String? = null,
    var password    : String? = null
)