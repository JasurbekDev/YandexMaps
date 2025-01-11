package com.idyllic.map_imp.usecase

import com.idyllic.core_api.model.ResourceUI
import com.idyllic.map_api.repository.MapRepository
import com.idyllic.map_api.usecase.FullAddressUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FullAddressUseCaseImp @Inject constructor(
    private val mapRepository: MapRepository
) : FullAddressUseCase {
    override suspend fun invoke(lat: Double, lon: Double): Flow<ResourceUI<String>> =
        mapRepository.getFullAddress(lat, lon)
}