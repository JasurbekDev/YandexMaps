package com.idyllic.core_imp.model

import androidx.annotation.Keep
import com.idyllic.core_api.model.LinesDto

@Keep
data class Lines(
    var lines : List<Line>?         = arrayListOf(),
    var meta  : BaseResponse2.Meta? = BaseResponse2.Meta()
){
    constructor(dto: LinesDto) : this(
        lines = dto.lines?.map { Line(it) },
        meta  = dto.meta?.let { BaseResponse2.Meta(it) }
    )

    fun toDto() = LinesDto(
        lines = lines?.map { it.toDto() },
        meta    = meta?.toDto()
    )
}