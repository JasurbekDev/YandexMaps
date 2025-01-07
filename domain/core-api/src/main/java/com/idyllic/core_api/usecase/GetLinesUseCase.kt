package com.idyllic.core_api.usecase

import com.idyllic.core_api.model.LinesBodyDto
import com.idyllic.core_api.model.LinesDto
import com.idyllic.core_api.model.ResourceUI
import com.idyllic.core_api.model.LoginBodyDto
import com.idyllic.core_api.model.UserDto
import kotlinx.coroutines.flow.Flow

interface GetLinesUseCase {

    suspend fun invoke(dto: LinesBodyDto): Flow<ResourceUI<LinesDto>>

}