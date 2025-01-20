package com.idyllic.yandexmaps.models

import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.GeoObjectSelectionMetadata
import java.io.Serializable

data class GeoObjectLocation(
    var name     : String? = null,
    var address  : String? = null,
    var rating   : Int?    = null,
    var reviews  : Int?    = null,
    var lat      : Double? = null,
    var lon      : Double? = null
) : Serializable