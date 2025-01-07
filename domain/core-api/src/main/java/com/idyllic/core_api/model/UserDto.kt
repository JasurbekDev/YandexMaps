package com.idyllic.core_api.model

data class UserDto(
    var createdAt: Int? = null,
    var firstName: String? = null,
    var id: Int? = null,
    var lastName: String? = null,
    var phoneNumber: String? = null,
    var role: Int? = null,
    var status: Int? = null,
    var step: Int? = null,
    var token: String? = null,
    var updatedAt: Int? = null,
    var isOrder: Boolean? = null,
)