package com.idyllic.map_api.source

import com.idyllic.core_api.model.ResourceUI
import kotlinx.coroutines.flow.Flow


interface MapDataSource {
    suspend fun getFullAddress(lat: Double, lon: Double): Flow<ResourceUI<String>>
}