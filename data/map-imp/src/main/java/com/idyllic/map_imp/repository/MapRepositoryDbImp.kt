package com.idyllic.map_imp.repository

import com.idyllic.map_api.model.LocationDto
import com.idyllic.map_api.repository.MapRepositoryDb
import com.idyllic.map_api.source.MapDataSourceDb
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MapRepositoryDbImp @Inject constructor(
    private val mapDataSourceDb: MapDataSourceDb
) : MapRepositoryDb {
    override suspend fun getLocations(): Flow<List<LocationDto>> {
        return mapDataSourceDb.getLocations()
    }

    override suspend fun insertLocations(locations: List<LocationDto>) {
        mapDataSourceDb.insertLocations(locations)
    }

    override suspend fun deleteByLatLon(latitude: Double, longitude: Double) {
        mapDataSourceDb.deleteByLatLon(latitude, longitude)
    }

}