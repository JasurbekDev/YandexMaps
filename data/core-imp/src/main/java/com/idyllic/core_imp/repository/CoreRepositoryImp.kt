package com.idyllic.core_imp.repository

import com.idyllic.core_api.model.LinesBodyDto
import com.idyllic.core_api.model.LinesDto
import com.idyllic.core_api.model.ProductDto
import com.idyllic.core_api.model.ResourceUI
import com.idyllic.core_api.model.LoginBodyDto
import com.idyllic.core_api.model.UserDto
import com.idyllic.core_api.repository.CoreRepository
import com.idyllic.core_api.source.CoreDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CoreRepositoryImp @Inject constructor(
    private val coreDataSource: CoreDataSource
) : CoreRepository {
    override suspend fun getProduct(id: Int): Flow<ResourceUI<ProductDto>> =
        withContext(Dispatchers.IO) {
            coreDataSource.getProduct(id)
        }

    override suspend fun login(dto: LoginBodyDto): Flow<ResourceUI<UserDto>> =
        withContext(Dispatchers.IO) {
            coreDataSource.login(dto)
        }

    override suspend fun getLines(dto: LinesBodyDto): Flow<ResourceUI<LinesDto>> =
        withContext(Dispatchers.IO) {
            coreDataSource.getLines(dto)
        }
}