package com.idyllic.map_imp.usecase

import com.idyllic.map_api.model.LocationDto
import com.idyllic.map_api.repository.MapRepositoryDb
import com.idyllic.map_api.usecase.GetLocationsDbUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLocationsDbUseCaseImp @Inject constructor(
    private val repositoryDb: MapRepositoryDb
) : GetLocationsDbUseCase {
    override suspend fun invoke(): Flow<List<LocationDto>> {
        return repositoryDb.getLocations()
    }
}