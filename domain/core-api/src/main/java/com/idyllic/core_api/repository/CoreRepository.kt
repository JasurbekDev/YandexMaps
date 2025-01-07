package com.idyllic.core_api.repository

import com.idyllic.core_api.model.LinesBodyDto
import com.idyllic.core_api.model.LinesDto
import com.idyllic.core_api.model.ProductDto
import com.idyllic.core_api.model.ResourceUI
import com.idyllic.core_api.model.LoginBodyDto
import com.idyllic.core_api.model.UserDto
import kotlinx.coroutines.flow.Flow

interface CoreRepository {

    suspend fun getProduct(id: Int): Flow<ResourceUI<ProductDto>>
    suspend fun login(dto: LoginBodyDto): Flow<ResourceUI<UserDto>>
    suspend fun getLines(dto: LinesBodyDto): Flow<ResourceUI<LinesDto>>

}