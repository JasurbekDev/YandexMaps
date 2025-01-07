package com.idyllic.core_api.model

data class LinesDto(
    var lines : List<LineDto>? = arrayListOf(),
    var meta  : MetaDto?       = MetaDto()
)