package com.idyllic.map_api.source

import com.idyllic.map_api.model.LocationDto
import kotlinx.coroutines.flow.Flow


interface MapDataSourceDb {
    suspend fun getLocations(): Flow<List<LocationDto>>
    suspend fun insertLocations(locations: List<LocationDto>)
    suspend fun deleteByLatLon(latitude: Double, longitude: Double)
}