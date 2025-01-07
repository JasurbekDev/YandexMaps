package com.idyllic.core_imp.usecase

import com.idyllic.core_api.model.ResourceUI
import com.idyllic.core_api.model.LoginBodyDto
import com.idyllic.core_api.model.UserDto
import com.idyllic.core_api.repository.CoreRepository
import com.idyllic.core_api.usecase.LoginUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCaseImp @Inject constructor(
    private val coreRepository: CoreRepository
) : LoginUseCase {
    override suspend fun invoke(dto: LoginBodyDto): Flow<ResourceUI<UserDto>> {
        return coreRepository.login(dto)
    }
}