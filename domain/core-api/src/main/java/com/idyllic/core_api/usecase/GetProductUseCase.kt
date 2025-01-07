package com.idyllic.core_api.usecase

import com.idyllic.core_api.model.ProductDto
import com.idyllic.core_api.model.ResourceUI
import kotlinx.coroutines.flow.Flow

interface GetProductUseCase {

    suspend fun invoke(id: Int): Flow<ResourceUI<ProductDto>>

}