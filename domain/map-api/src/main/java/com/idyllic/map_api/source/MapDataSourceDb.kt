package com.idyllic.map_api.source

import com.idyllic.map_api.model.LocationDto


interface MapDataSourceDb {
    suspend fun getLocations(): List<LocationDto>
    suspend fun insertLocations(locations: List<LocationDto>)
    suspend fun deleteByLatLon(latitude: Double, longitude: Double)
}