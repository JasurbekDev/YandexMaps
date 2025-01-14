package com.idyllic.map_api.usecase

interface DeleteLocationDbUseCase {
    suspend fun invoke(latitude: Double, longitude: Double)
}