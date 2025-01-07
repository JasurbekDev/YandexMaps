package com.idyllic.core_imp.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.idyllic.core_api.model.UserDto

@Keep
data class User(
    @SerializedName("created_at"   ) var createdAt   : Int?     = null,
    @SerializedName("first_name"   ) var firstName   : String?  = null,
    @SerializedName("id"           ) var id          : Int?     = null,
    @SerializedName("last_name"    ) var lastName    : String?  = null,
    @SerializedName("phone_number" ) var phoneNumber : String?  = null,
    @SerializedName("role"         ) var role        : Int?     = null,
    @SerializedName("status"       ) var status      : Int?     = null,
    @SerializedName("step"         ) var step        : Int?     = null,
    @SerializedName("token"        ) var token       : String?  = null,
    @SerializedName("updated_at"   ) var updatedAt   : Int?     = null,
    @SerializedName("is_order"     ) var isOrder     : Boolean? = null
) {

    constructor(dto: UserDto?) : this(
        dto?.createdAt,
        dto?.firstName,
        dto?.id,
        dto?.lastName,
        dto?.phoneNumber,
        dto?.role,
        dto?.status,
        dto?.step,
        dto?.token,
        dto?.updatedAt,
        dto?.isOrder
    )

    fun toDto() = UserDto(
        createdAt,
        firstName,
        id,
        lastName,
        phoneNumber,
        role,
        status,
        step,
        token,
        updatedAt,
        isOrder
    )

}