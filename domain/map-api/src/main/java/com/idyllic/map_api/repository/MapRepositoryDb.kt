package com.idyllic.map_api.repository

import com.idyllic.map_api.model.LocationDto

interface MapRepositoryDb {
    suspend fun getLocations(): List<LocationDto>
    suspend fun insertLocations(locations: List<LocationDto>)
    suspend fun deleteByLatLon(latitude: Double, longitude: Double)
}