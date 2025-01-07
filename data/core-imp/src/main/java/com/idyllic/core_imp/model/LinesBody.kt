package com.idyllic.core_imp.model

import androidx.annotation.Keep
import com.idyllic.core_api.model.LinesBodyDto

@Keep
data class LinesBody(
    var page : Int? = null
) {

    constructor(dto: LinesBodyDto) : this(
        page = dto.page
    )

    fun toDto() = LinesBodyDto(
        page = page
    )

}