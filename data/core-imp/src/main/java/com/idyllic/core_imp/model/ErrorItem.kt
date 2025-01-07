package com.idyllic.core_imp.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
@Keep
data class ErrorItem(
    @SerializedName("message"   ) var message   : String? = null,
    @SerializedName("code"      ) var code      : String? = null
)