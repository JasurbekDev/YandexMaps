package com.idyllic.map_api.model

import java.io.Serializable

data class LocationDto(
    var name     : String? = null,
    var street   : String? = null,
    var distance : Int?    = null,
    var lat      : Double? = null,
    var lon      : Double? = null,
    var rating   : Int?    = null,
    var reviews  : Int?    = null
) : Serializable