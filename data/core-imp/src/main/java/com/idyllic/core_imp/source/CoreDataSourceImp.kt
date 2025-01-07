package com.idyllic.core_imp.source

import com.idyllic.core_api.model.LinesBodyDto
import com.idyllic.core_api.model.LinesDto
import com.idyllic.core_api.model.ProductDto
import com.idyllic.core_api.model.ResourceUI
import com.idyllic.core_api.model.LoginBodyDto
import com.idyllic.core_api.model.UserDto
import com.idyllic.core_api.source.CoreDataSource
import com.idyllic.core_imp.model.Lines
import com.idyllic.core_imp.model.LinesBody
import com.idyllic.core_imp.model.LoginBody
import com.idyllic.core_imp.remote.CoreService
import com.idyllic.core_imp.util.awaitGeneral
import com.idyllic.core_imp.util.awaitGeneralForList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CoreDataSourceImp @Inject constructor(
    private val service: CoreService
) : CoreDataSource {
    override suspend fun getProduct(id: Int): Flow<ResourceUI<ProductDto>> = flow {
//        emit(service.getProducts(id).awaitGeneral { it.toDto() })
    }

    override suspend fun login(dto: LoginBodyDto): Flow<ResourceUI<UserDto>> = flow {
        emit(service.login(LoginBody(dto)).awaitGeneral { it.toDto() })
    }

    override suspend fun getLines(dto: LinesBodyDto): Flow<ResourceUI<LinesDto>> = flow {
        emit(service.getLines(LinesBody(dto).page).awaitGeneralForList {
            val lines = Lines(
                lines = it.data,
                meta = it.meta
            )
            lines.toDto()
        })
    }
}