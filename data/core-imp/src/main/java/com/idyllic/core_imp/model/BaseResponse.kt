package com.idyllic.core_imp.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
@Keep
data class BaseResponse<T>(
    @SerializedName("success" ) var success : Boolean?     = null,
    @SerializedName("data"    ) var data    : T?           = null,
    @SerializedName("code"    ) var code    : Int?         = null,
    @SerializedName("error"   ) var error   : ErrorItem?   = ErrorItem()
)