package com.idyllic.map_api.usecase

import com.idyllic.map_api.model.LocationDto
import kotlinx.coroutines.flow.Flow

interface GetLocationsDbUseCase {
    suspend fun invoke(): Flow<List<LocationDto>>
}