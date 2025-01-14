package com.idyllic.map_api.usecase

import com.idyllic.core_api.model.ResourceUI
import com.idyllic.map_api.model.LocationDto
import kotlinx.coroutines.flow.Flow

interface SearchByTextUseCase {
    suspend fun invoke(query: String, lat: Double, lon: Double): Flow<ResourceUI<List<LocationDto>>>
}