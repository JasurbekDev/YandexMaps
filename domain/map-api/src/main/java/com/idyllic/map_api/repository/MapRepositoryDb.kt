package com.idyllic.map_api.repository

import com.idyllic.map_api.model.LocationDto
import kotlinx.coroutines.flow.Flow

interface MapRepositoryDb {
    suspend fun getLocations(): Flow<List<LocationDto>>
    suspend fun insertLocations(locations: List<LocationDto>)
    suspend fun deleteByLatLon(latitude: Double, longitude: Double)
}