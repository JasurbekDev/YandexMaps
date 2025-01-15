package com.idyllic.map_imp.source

import com.idyllic.map_api.model.LocationDto
import com.idyllic.map_api.source.MapDataSourceDb
import com.idyllic.map_imp.local.MapServiceDao
import com.idyllic.map_imp.model.LocationEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MapDataSourceDbImp @Inject constructor(
    private val mapServiceDao: MapServiceDao
) : MapDataSourceDb {
    override suspend fun getLocations(): Flow<List<LocationDto>> {
        return mapServiceDao.getLocations().map { locations ->
            locations.map { it.toDto() }
        }
    }

    override suspend fun insertLocations(locations: List<LocationDto>) {
        mapServiceDao.insertLocations(locations.map { LocationEntry(it) })
    }

    override suspend fun deleteByLatLon(latitude: Double, longitude: Double) {
        mapServiceDao.deleteByLatLon(latitude, longitude)
    }

}