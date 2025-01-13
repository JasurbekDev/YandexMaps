package com.idyllic.map_api.model

import java.io.Serializable

data class LocationDto(
    var name     : String? = null,
    var street   : String? = null,
    var distance : String? = null
) : Serializable