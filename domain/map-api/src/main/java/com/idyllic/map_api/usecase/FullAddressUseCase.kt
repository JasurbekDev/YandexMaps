package com.idyllic.map_api.usecase

import com.idyllic.core_api.model.ResourceUI
import kotlinx.coroutines.flow.Flow

interface FullAddressUseCase {
    suspend fun invoke(lat: Double, lon: Double): Flow<ResourceUI<String>>
}