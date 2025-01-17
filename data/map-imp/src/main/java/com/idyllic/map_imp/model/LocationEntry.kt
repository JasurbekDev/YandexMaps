package com.idyllic.map_imp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.idyllic.map_api.model.LocationDto

@Entity
data class LocationEntry(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo("name"     ) var name     : String? = null,
    @ColumnInfo("street"   ) var street   : String? = null,
    @ColumnInfo("distance" ) var distance : Int?    = null,
    @ColumnInfo("lat"      ) var lat      : Double? = null,
    @ColumnInfo("lon"      ) var lon      : Double? = null,
    @ColumnInfo("rating"   ) var rating   : Int?    = null,
    @ColumnInfo("reviews"  ) var reviews  : Int?    = null
) {
    constructor(dto: LocationDto) : this(
        0,
        dto.name,
        dto.street,
        dto.distance,
        dto.lat,
        dto.lon,
        dto.rating,
        dto.reviews
    )

    fun toDto() = LocationDto(
        name,
        street,
        distance,
        lat,
        lon,
        rating,
        reviews
    )
}