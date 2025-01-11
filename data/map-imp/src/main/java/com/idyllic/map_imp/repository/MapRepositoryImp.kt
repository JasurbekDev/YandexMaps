package com.idyllic.map_imp.repository

import com.idyllic.map_api.repository.MapRepository
import com.idyllic.map_api.source.MapDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MapRepositoryImp @Inject constructor(
    private val mapDataSource: MapDataSource
) : MapRepository {
    override suspend fun getFullAddress(lat: Double, lon: Double) = withContext(Dispatchers.IO) {
        mapDataSource.getFullAddress(lat, lon)
    }
}