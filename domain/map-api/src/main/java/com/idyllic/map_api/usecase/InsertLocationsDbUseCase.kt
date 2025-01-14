package com.idyllic.map_api.usecase

import com.idyllic.map_api.model.LocationDto

interface InsertLocationsDbUseCase {
    suspend fun invoke(locations: List<LocationDto>)
}