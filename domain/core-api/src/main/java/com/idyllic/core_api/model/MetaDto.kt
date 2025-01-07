package com.idyllic.core_api.model

data class MetaDto(
    var totalCount  : Int? = null,
    var pageCount   : Int? = null,
    var currentPage : Int? = null,
    var perPage     : Int? = null
)