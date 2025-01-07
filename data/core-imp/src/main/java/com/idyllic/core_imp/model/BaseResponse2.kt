package com.idyllic.core_imp.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.idyllic.core_api.model.LineDto
import com.idyllic.core_api.model.MetaDto
import kotlinx.serialization.SerialName
@Keep
data class BaseResponse2<T>(
    @SerializedName("code"    ) var code    : Int?    = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("data"    ) var data    : T?      = null,
    @SerializedName("error"   ) var error   : String? = null,
    @SerializedName("meta"    ) var meta    : Meta?   = null
) {
    @Keep
    data class Meta(
        @SerializedName("totalCount"  ) var totalCount  : Int? = null,
        @SerializedName("pageCount"   ) var pageCount   : Int? = null,
        @SerializedName("currentPage" ) var currentPage : Int? = null,
        @SerializedName("perPage"     ) var perPage     : Int? = null
    ) {

        constructor(dto: MetaDto?) : this(
            dto?.totalCount,
            dto?.pageCount,
            dto?.currentPage,
            dto?.perPage
        )

        fun toDto() = MetaDto(
            totalCount,
            pageCount,
            currentPage,
            perPage
        )

    }
}