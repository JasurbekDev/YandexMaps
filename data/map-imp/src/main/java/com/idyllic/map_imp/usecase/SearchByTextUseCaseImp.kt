package com.idyllic.map_imp.usecase

import com.idyllic.core_api.model.ResourceUI
import com.idyllic.map_api.model.LocationDto
import com.idyllic.map_api.repository.MapRepository
import com.idyllic.map_api.usecase.SearchByTextUseCase
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class SearchByTextUseCaseImp @Inject constructor(
    private val mapRepository: MapRepository
) : SearchByTextUseCase {
    override suspend fun invoke(query: String, lat: Double, lon: Double): Flow<ResourceUI<List<LocationDto>>> =
        mapRepository.searchByText(query, lat, lon)
}