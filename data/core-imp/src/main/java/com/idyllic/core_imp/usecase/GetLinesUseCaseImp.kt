package com.idyllic.core_imp.usecase

import com.idyllic.core_api.model.LinesBodyDto
import com.idyllic.core_api.model.LinesDto
import com.idyllic.core_api.model.ResourceUI
import com.idyllic.core_api.model.LoginBodyDto
import com.idyllic.core_api.model.UserDto
import com.idyllic.core_api.repository.CoreRepository
import com.idyllic.core_api.usecase.GetLinesUseCase
import com.idyllic.core_api.usecase.LoginUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLinesUseCaseImp @Inject constructor(
    private val coreRepository: CoreRepository
) : GetLinesUseCase {
    override suspend fun invoke(dto: LinesBodyDto): Flow<ResourceUI<LinesDto>> {
        return coreRepository.getLines(dto)
    }
}