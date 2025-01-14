package com.idyllic.map_imp.usecase

import com.idyllic.map_api.repository.MapRepositoryDb
import com.idyllic.map_api.usecase.DeleteLocationDbUseCase
import javax.inject.Inject

class DeleteLocationDbUseCaseImp @Inject constructor(
    private val repositoryDb: MapRepositoryDb
) : DeleteLocationDbUseCase {
    override suspend fun invoke(latitude: Double, longitude: Double) {
        return repositoryDb.deleteByLatLon(latitude, longitude)
    }
}