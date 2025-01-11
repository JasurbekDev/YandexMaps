package com.idyllic.yandexmaps.models

import java.io.Serializable

data class GeoObjectLocation(
    var name    : String? = null,
    var address : String? = null,
    var rating  : Int?    = null,
    var reviews : Int?    = null
) : Serializable