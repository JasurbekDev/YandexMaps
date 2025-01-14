package com.idyllic.map_api.usecase

import com.idyllic.map_api.model.LocationDto

interface GetLocationsDbUseCase {
    suspend fun invoke(): List<LocationDto>
}