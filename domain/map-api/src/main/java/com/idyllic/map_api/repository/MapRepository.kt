package com.idyllic.map_api.repository

import com.idyllic.core_api.model.ResourceUI
import kotlinx.coroutines.flow.Flow

interface MapRepository {
    suspend fun getFullAddress(lat: Double, lon: Double): Flow<ResourceUI<String>>
}