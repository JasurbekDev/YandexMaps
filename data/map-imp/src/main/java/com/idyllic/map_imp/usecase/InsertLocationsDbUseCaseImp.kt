package com.idyllic.map_imp.usecase

import com.idyllic.map_api.model.LocationDto
import com.idyllic.map_api.repository.MapRepositoryDb
import com.idyllic.map_api.usecase.InsertLocationsDbUseCase
import javax.inject.Inject

class InsertLocationsDbUseCaseImp @Inject constructor(
    private val repositoryDb: MapRepositoryDb
) : InsertLocationsDbUseCase {
    override suspend fun invoke(locations: List<LocationDto>) {
        return repositoryDb.insertLocations(locations)
    }
}