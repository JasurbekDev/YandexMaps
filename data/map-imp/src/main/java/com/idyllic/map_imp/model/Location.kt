package com.idyllic.map_imp.model

import androidx.annotation.Keep
import com.idyllic.map_api.model.LocationDto

@Keep
data class Location(
    var name     : String? = null,
    var street   : String? = null,
    var distance : Int?    = null,
    var lat      : Double? = null,
    var lon      : Double? = null
) {
    fun toDto() = LocationDto(
        name,
        street,
        distance,
        lat,
        lon
    )
}