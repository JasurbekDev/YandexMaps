package com.idyllic.core_imp.usecase

import com.idyllic.core_api.model.ProductDto
import com.idyllic.core_api.model.ResourceUI
import com.idyllic.core_api.repository.CoreRepository
import com.idyllic.core_api.usecase.GetProductUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductUseCaseImp @Inject constructor(
    private val coreRepository: CoreRepository
) : GetProductUseCase {
    override suspend fun invoke(id: Int): Flow<ResourceUI<ProductDto>> {
        return coreRepository.getProduct(id)
    }
}